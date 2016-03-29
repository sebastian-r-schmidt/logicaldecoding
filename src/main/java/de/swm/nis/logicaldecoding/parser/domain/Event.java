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
package de.swm.nis.logicaldecoding.parser.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

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
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
	    .appendPattern("yyyy-MM-dd HH:mm:ss")
	    .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 3, true)
	    .appendPattern("x")
	    .toFormatter();
		commitTime = ZonedDateTime.parse(timestampString,formatter);
	}
	
	public ZonedDateTime getCommitTime() {
		return commitTime;
	}
	
}
