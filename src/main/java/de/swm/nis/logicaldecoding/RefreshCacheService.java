/*
Copyright (c) 2016 Sebastian Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package de.swm.nis.logicaldecoding;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import de.swm.nis.logicaldecoding.dataaccess.ChangeSetDAO;
import de.swm.nis.logicaldecoding.dataaccess.ChangeSetFetcher;
import de.swm.nis.logicaldecoding.gwc.GWCInvalidator;
import de.swm.nis.logicaldecoding.parser.PgParser;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;
import de.swm.nis.logicaldecoding.parser.domain.Event;
import de.swm.nis.logicaldecoding.parser.domain.TxEvent;
import de.swm.nis.logicaldecoding.tracktable.TrackTablePublisher;



@Service
@EnableAsync
public class RefreshCacheService {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheService.class);

	@Autowired
	private ChangeSetFetcher changesetFetcher;

	@Autowired
	private GWCInvalidator gwcInvalidator;

	@Autowired
	private PgParser parser;

	@Autowired
	private TrackTablePublisher trackTablePublisher;

	@Value("${scheduling.numChangesToFetch}")
	private int numChangestoFetch;

	@Value("${scheduling.interval}")
	private int schedulingDelay;

	@Value("#{'${filter.includeSchema}'.split(',')}")
	private List<String> schemasToInclude;

	@Value("${gwc.doSeed}")
	private boolean doSeed;

	@Value("${tracktable.doPublish}")
	private boolean doPublish;
	
	@Value("${postgresql.replicationSlotName}")
	private String replicationSlotName;
	
	@Autowired
	private VersionHelper versionHelper;



	@PostConstruct
	public void init() {
		log.info(versionHelper.determineVersion());
		log.info("looking for changes in Database every " + schedulingDelay + " milliseconds");
	}



	@Scheduled(fixedDelayString = "${scheduling.interval}")
	public void refreshCache() {

		log.debug("Start pulling changes...");
		Collection<ChangeSetDAO> changes = changesetFetcher.fetch(replicationSlotName, numChangestoFetch);
		Collection<DmlEvent> events = new ArrayList<DmlEvent>();
		Map<Long,ZonedDateTime> transactionCommitTimes = new HashMap<Long, ZonedDateTime>();
		
		for (ChangeSetDAO change : changes) {
			log.info(change.toString());
			Event event = parser.parseLogLine(change.getData());
			event.setTransactionId(change.getTransactionId());
			// This is a change to consider
			if (event instanceof DmlEvent) {
				events.add((DmlEvent)event);
			}
			else if (event instanceof TxEvent) {
				if (event.getCommitTime() !=null) {
					transactionCommitTimes.put(event.getTransactionId(), event.getCommitTime());
				}
			}
		}
		
		for(DmlEvent event : events) {
			event.setCommitTime(transactionCommitTimes.get(event.getTransactionId()));
		}
		
		Predicate<DmlEvent> predicate = new Predicate<DmlEvent>() {
			@Override
			public boolean apply(DmlEvent input) {
				return schemasToInclude.contains(input.getSchemaName());
			}
		};

		Collection<DmlEvent> relevantEvents = Collections2.filter(events, predicate);
		log.info("Pulled changes [max:" + numChangestoFetch + ", found:"+events.size() + ", relevant:"+relevantEvents.size() + "]");

		if (relevantEvents.size() == 0) {
			// Nothing to do
			return;
		}

		Future<String> seeding = null;
		Future<String> publishing = null;
		if (doSeed) {
			seeding = gwcInvalidator.postSeedRequests(relevantEvents);
		}
		if (doPublish) {
			publishing = trackTablePublisher.publish(relevantEvents);
		}

		// Wait for Publish task to be finished and check for Errors
		if (doPublish) {
			while (!(publishing.isDone())) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					log.warn("InterruptedExcepton during waiting of completion of Tasks", e);
				}
			}
			try {
				publishing.get();
			} catch (InterruptedException e) {
				log.warn("InterruptedExcepton during excecution of 'publishing' Task", e);
			} catch (ExecutionException e) {
				log.warn("Exception occurrred during execution of 'publishing' Task.", e);
			}
		}

		// Wait for Seed task to be finished and check for Errors
		if (doSeed) {
			while (!(seeding.isDone())) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					log.warn("InterruptedExcepton during waiting of completion of Tasks", e);
				}
			}
			try {
				seeding.get();
			} catch (InterruptedException e) {
				log.warn("InterruptedExcepton during excecution of 'seeding' Task", e);
			} catch (ExecutionException e) {
				log.warn("Exception occurrred during execution of 'seeding' Task.", e);
			}
		}
	}

}
