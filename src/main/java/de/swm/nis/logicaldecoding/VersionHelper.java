package de.swm.nis.logicaldecoding;

import org.springframework.stereotype.Component;

@Component
public class VersionHelper {
	
	public String determineVersion() {
		return "LogicalDeoding v.1.0.1-SNAPSHOT";
	}

}
