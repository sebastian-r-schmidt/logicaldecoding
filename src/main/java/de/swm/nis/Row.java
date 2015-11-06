package de.swm.nis;

import java.util.ArrayList;
import java.util.List;



public class Row {

	public enum Type {
		insert, update, delete
	};

	private List<Cell> oldValues;
	private List<Cell> newValues;
	private String tableName;
	private final Type type;



	public Row(String table, Row.Type type) {
		this.type = type;
		this.tableName = table;
		oldValues = new ArrayList<Cell>();
		newValues = new ArrayList<Cell>();
	}



	public List<Cell> getOldValues() {
		return oldValues;
	}



	public List<Cell> getNewValues() {
		return newValues;
	}



	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public Type getType() {
		return type;
	}



	@Override
	public String toString() {
		return "Row [type=" + type + ", tableName=" + tableName + ", newValues=" + newValues + ", oldValues="
				+ oldValues + "]";
	}

}
