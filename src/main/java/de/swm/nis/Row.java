package de.swm.nis;

import java.util.ArrayList;
import java.util.List;

public class Row {
	
	private List<Cell> oldValues;
	private List<Cell> newValues;
	private String tableName;
	
	public Row() {
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

	@Override
	public String toString() {
		return "Row [tableName=" + tableName + ", oldValues=" + oldValues + ", newValues=" + newValues + "]";
	}

}
