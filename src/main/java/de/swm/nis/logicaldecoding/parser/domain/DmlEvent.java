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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;



/**
 * Represents a changed Row in a Database. Contains old an new values.
 * @author Schmidt.Sebastian2
 *
 */
public class DmlEvent extends Event{

	private static final Logger log = LoggerFactory.getLogger(DmlEvent.class);
	
	public enum Type {
		insert, update, delete
	};

	private List<Cell> oldValues;
	private List<Cell> newValues;
	private final Type type;



	public DmlEvent(String schema, String table, DmlEvent.Type type) {
		this.type = type;
		setTableName(table);
		setSchemaName(schema);
		oldValues = new ArrayList<Cell>();
		newValues = new ArrayList<Cell>();
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

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Row [type=" + type + ", tableName=" + getTableName() + ", newValues=" + newValues + ", oldValues="
				+ oldValues + "]";
	}

}
