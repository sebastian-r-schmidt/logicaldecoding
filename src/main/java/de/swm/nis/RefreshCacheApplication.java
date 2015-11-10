package de.swm.nis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.vividsolutions.jts.geom.Envelope;

import de.swm.nis.gwc.GWCInvalidator;



@SpringBootApplication
public class RefreshCacheApplication {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheApplication.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(RefreshCacheApplication.class, args);
		
		ChangeSetFetcher fetcher = (ChangeSetFetcher) ctx.getBean("changeSetFetcher");
		GWCInvalidator invalidator = (GWCInvalidator) ctx.getBean("GWCInvalidator");

		//TODO build real components
		
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//		for (String name:beanNames) {
//			log.info(name);
//		}
		
		long pending_changes = fetcher.peek("repslot_test");

		log.info("Changes pending to process: " + pending_changes);

		if (pending_changes > 0) {
			
			LogParser parser = new LogParser();
			
			log.info("fetching up to 20 changes...");
			List<ChangeSet> changes = fetcher.fetch("repslot_test", 20);
			List<Row> rows = new ArrayList<Row>();

			for (ChangeSet change : changes) {
				log.info(change.toString());
				Row row = parser.parseLogLine(change.getData());
				//This is a change to consider
				if (row!=null) {
					rows.add(row);
				}
			}
			log.info("Calculating affected Regions...");
			RegionParser regionParser = new RegionParser();
			List<Envelope> envelopes = regionParser.findAffectedRegion(rows);
			for (Envelope env:envelopes) {
				log.info(env.toString());
				invalidator.postReseed(env);
			}
		}

	}
}
