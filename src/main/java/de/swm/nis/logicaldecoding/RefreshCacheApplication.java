package de.swm.nis.logicaldecoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;



@SpringBootApplication
@EnableScheduling
public class RefreshCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefreshCacheApplication.class, args);
	}
}
