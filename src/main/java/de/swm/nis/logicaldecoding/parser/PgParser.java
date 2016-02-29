package de.swm.nis.logicaldecoding.parser;

import de.swm.nis.logicaldecoding.parser.domain.Event;

public interface PgParser {

	public Event parseLogLine(String message);
	
}
