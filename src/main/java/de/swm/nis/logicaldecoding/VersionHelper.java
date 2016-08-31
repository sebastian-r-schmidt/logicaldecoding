package de.swm.nis.logicaldecoding;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class VersionHelper {
	
	@Autowired
	ApplicationContext ctx;
	
	private static final Logger log = LoggerFactory.getLogger(RefreshCacheService.class);
	
	public String determineVersion() {
		return "LogicalDecoding v("+ determineMavenVersion() + ") - git: "+determineGitVersion();
	}

	public String determineGitVersion() {
		String gitVersion = "";
		Resource resource = ctx.getResource("classpath:git.properties");
		try {
			Properties gitProperties = PropertiesLoaderUtils.loadProperties(resource);
			gitVersion = (String) gitProperties.get("git.commit.id.describe");
		} catch (IOException e) {
			log.warn("IOException while determining git version", e);
		}
		return gitVersion;
	}
	
	public String determineMavenVersion() {
		String mavenVersion = "";
		Resource resource = ctx.getResource("classpath:META-INF/MANIFEST.MF");
		try {
			Properties mavenProperties = PropertiesLoaderUtils.loadProperties(resource);
			mavenVersion = (String) mavenProperties.get("Implementation-Version");
		} catch (IOException e) {
			log.warn("IOException while determining maven version", e);
		}
		return mavenVersion;
	}
	
}
