package de.swm.nis.logicaldecoding.parser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;



/**
 * Represents a changed Row in a Database. Contains old an new values.
 * @author Schmidt.Sebastian2
 *
 */
public class Row {

	private static final Logger log = LoggerFactory.getLogger(Row.class);
	
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

	public String getSchemaName() {
		if (tableName !=null) {
			return Iterables.get(Splitter.on('.').split(tableName),0);
		}
		throw new RuntimeException("tableName is null, cannot calculate Schema name");
	}
	
	public Envelope getEnvelope() {
		
		try {
		Envelope envelope = new Envelope();
		for (Cell cell:getOldValues()) {
			if (cell.getType().equals(Cell.Type.geometry)) {
				if (!cell.getValue().equals("null")) {
					Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
					envelope.expandToInclude(geom.getEnvelopeInternal());
				}
			}
		}
		for (Cell cell:getNewValues()) {
			if (cell.getType().equals(Cell.Type.geometry)) {
				if (!cell.getValue().equals("null")) {
					Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
					envelope.expandToInclude(geom.getEnvelopeInternal());
				}
			}
		}
		return envelope;
		} catch (ParseException e) {
			log.warn("ParseException on parsing WKB Geometry, ", e);
			throw new RuntimeException(e);
		}
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
