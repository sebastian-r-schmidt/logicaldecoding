package de.swm.nis.logicaldecoding.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.LoglineContext;
import de.swm.nis.logicaldecoding.parser.domain.Event;

public class AntlrBasedParser implements PgParser {

	public Event parseLogLine(String message) {
		PgLogicalDecodingLexer lexer = new PgLogicalDecodingLexer(
				new ANTLRInputStream(message));
		PgLogicalDecodingParser parser = new PgLogicalDecodingParser(
				new CommonTokenStream(lexer));
		LoglineContext tree = parser.logline();
		ParserListener parserListener = new ParserListener();
		ParseTreeWalker.DEFAULT.walk(parserListener, tree);
		return parserListener.getEvent();
	}
	
}
