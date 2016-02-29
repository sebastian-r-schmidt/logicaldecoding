package de.swm.nis.logicaldecoding.parser.domain;

public abstract class Event {
	
	private String schemaName;
	private String tableName;
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
		
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
		
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public String getTableName() {
		return tableName;
	}
}
