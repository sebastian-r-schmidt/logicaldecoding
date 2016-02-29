package de.swm.nis.logicaldecoding.parser;

import org.antlr.v4.runtime.misc.NotNull;

import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.DeleteOpContext;
import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.InsertOpContext;
import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.TxStatementContext;
import de.swm.nis.logicaldecoding.parser.PgLogicalDecodingParser.UpdateOpContext;
import de.swm.nis.logicaldecoding.parser.domain.Cell;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;
import de.swm.nis.logicaldecoding.parser.domain.Event;
import de.swm.nis.logicaldecoding.parser.domain.TxEvent;

public class ParserListener extends PgLogicalDecodingBaseListener {


	private Event currentEvent;
	
	private String tableName;
	private String schemaName;
	
	private Cell currentKeyValuePair;

	@Override
	public void enterTxStatement(TxStatementContext ctx) {
		currentEvent = new TxEvent();
	}

	@Override
	public void enterInsertOp(InsertOpContext ctx) {
		currentEvent = new DmlEvent(schemaName, tableName, DmlEvent.Type.insert);
	}

	@Override
	public void enterUpdateOp(UpdateOpContext ctx) {
		currentEvent = new DmlEvent(schemaName, tableName, DmlEvent.Type.update);
	}

	@Override
	public void enterDeleteOp(DeleteOpContext ctx) {
		currentEvent = new DmlEvent(schemaName, tableName, DmlEvent.Type.delete);
	}

	public void enterSchemaname(@NotNull PgLogicalDecodingParser.SchemanameContext ctx) { 
		 schemaName = ctx.Identifier().getText();
	}
	
	public void enterTablename(@NotNull PgLogicalDecodingParser.TablenameContext ctx) {
		tableName = ctx.Identifier().getText();
	}
	
	@Override public void enterOldKeyValuePair(@NotNull PgLogicalDecodingParser.OldKeyValuePairContext ctx) {
		currentKeyValuePair = new Cell();
	}
	
	@Override public void enterNewKeyValuePair(@NotNull PgLogicalDecodingParser.NewKeyValuePairContext ctx) {
		currentKeyValuePair = new Cell();
	}
	
	@Override public void exitNewKeyValuePair(@NotNull PgLogicalDecodingParser.NewKeyValuePairContext ctx) {
		((DmlEvent)currentEvent).getNewValues().add(currentKeyValuePair);
	}
	
	@Override public void exitOldKeyValuePair(@NotNull PgLogicalDecodingParser.OldKeyValuePairContext ctx) {
		((DmlEvent)currentEvent).getOldValues().add(currentKeyValuePair);
	}
	
	@Override public void enterColumnname(@NotNull PgLogicalDecodingParser.ColumnnameContext ctx) {
		currentKeyValuePair.setName(ctx.Identifier().getText());
	}
	
	@Override public void enterTypedef(@NotNull PgLogicalDecodingParser.TypedefContext ctx) {
		currentKeyValuePair.setType(ctx.getText());
	}
	
	@Override public void enterValue(@NotNull PgLogicalDecodingParser.ValueContext ctx) { 
		currentKeyValuePair.setValue(ctx.getText());
	}
	
	@Override public void enterQuotedValue(@NotNull PgLogicalDecodingParser.QuotedValueContext ctx) { 
		String value = ctx.getText();
		if (value.startsWith("'") && value.endsWith("'")) {
			int length = value.length();
			value = value.substring(1, length - 1);
		}
		currentKeyValuePair.setValue(value);
	}
	
	public Event getEvent() {
		return currentEvent;
	}
	
}