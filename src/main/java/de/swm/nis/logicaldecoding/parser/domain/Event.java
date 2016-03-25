package de.swm.nis.logicaldecoding.parser.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Event {
	
	private String schemaName;
	private String tableName;
	private long transactionId;
	private ZonedDateTime commitTime;
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;	
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public long getTransactionId() {
		return transactionId;
	}
	
	public void setCommitTime(ZonedDateTime commitTime) {
		this.commitTime = commitTime;
	}
	
	public void setCommitTime(String timestampString) {
		commitTime = ZonedDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssx"));
	}
	
	public ZonedDateTime getCommitTime() {
		return commitTime;
	}
	
}
