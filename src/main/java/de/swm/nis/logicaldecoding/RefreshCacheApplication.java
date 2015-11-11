package de.swm.nis.logicaldecoding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class RefreshCacheApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RefreshCacheApplication.class);
	
	
	public static void main(String[] args) {
		SpringApplication.run(RefreshCacheApplication.class, args);
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		log.info("looking for changes on the db every 2 minutes...");
	}
}
