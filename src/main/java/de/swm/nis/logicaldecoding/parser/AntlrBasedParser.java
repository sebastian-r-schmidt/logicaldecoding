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
package de.swm.nis.logicaldecoding.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.stereotype.Component;

import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.LoglineContext;
import de.swm.nis.logicaldecoding.parser.domain.Event;

@Component
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
