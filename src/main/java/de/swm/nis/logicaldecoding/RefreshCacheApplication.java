package de.swm.nis.logicaldecoding;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vividsolutions.jts.geom.Envelope;

import de.swm.nis.logicaldecoding.dataaccess.ChangeSetDAO;
import de.swm.nis.logicaldecoding.dataaccess.ChangeSetFetcher;
import de.swm.nis.logicaldecoding.gwc.GWCInvalidator;
import de.swm.nis.logicaldecoding.parser.LogicalDecodingTestPluginParser;
import de.swm.nis.logicaldecoding.parser.Row;



@SpringBootApplication
public class RefreshCacheApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheApplication.class);
	
	@Autowired
	private ChangeSetFetcher changesetFetcher;
	
	@Autowired
	private GWCInvalidator gwcInvalidator;
	
	@Autowired
	private BoundsCalculator boundsCalculator;
	
	@Autowired
	private LogicalDecodingTestPluginParser parser;
	
	public static void main(String[] args) {
		SpringApplication.run(RefreshCacheApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		refreshCache();
	}
	
	public void refreshCache() {
		
		long pending_changes = changesetFetcher.peek("repslot_test");

		log.info("Changes pending to process: " + pending_changes);

		if (pending_changes > 0) {
			
			log.info("fetching up to 20 changes...");
			List<ChangeSetDAO> changes = changesetFetcher.fetch("repslot_test", 20);
			List<Row> rows = new ArrayList<Row>();

			for (ChangeSetDAO change : changes) {
				log.info(change.toString());
				Row row = parser.parseLogLine(change.getData());
				//This is a change to consider
				if (row!=null) {
					rows.add(row);
				}
			}
			log.info("Calculating affected Regions...");
			List<Envelope> envelopes = boundsCalculator.findAffectedRegion(rows);
			log.info("sending " + envelopes.size() +" Seed Requests to geoWebCache...");
			for (Envelope env:envelopes) {
				log.info(env.toString());
				gwcInvalidator.postSeedRequest(env);
			}
		}

	}
}
