/*
 * Copyright 2016 SWM Services GmbH
 */

package de.swm.nis.logicaldecoding.tracktable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TrackTableTestConfiguration {
	

	@Bean
	public JdbcTemplate testTemplate() {
        EmbeddedDatabase db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql").build();
        return new JdbcTemplate(db);
	}
	

}
