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
package de.swm.nis.logicaldecoding.tracktable;

import java.sql.SQLException;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.postgresql.util.PGobject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBWriter;

import de.swm.nis.logicaldecoding.parser.domain.Cell;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;

/**
 * This class publishes changes into an audit table.
 * Old an new Values of the whole object record are stored in JSONB objects.
 * @author Schmidt.Sebastian2
 *
 */
@Repository
public class TrackTablePublisher {
	
	private static final Logger log = LoggerFactory.getLogger(TrackTablePublisher.class);
	
	@Autowired
	private JdbcTemplate template;
	
	@Value("${tracktable.schemaname}")
	private String schemaname;
	
	@Value("${tracktable.tablename}")
	private String tableName;
	
	@Value("#{'${tracktable.metainfo.searchpatterns}'.split(',')}")
	private List<String> metaInfoSearchPatterns;
	
	@Value("${postgresql.epsgCode}")
	private int epsgCode;
	
	@Async
	public Future<String> publish(Collection<DmlEvent> events) {
		log.info("Publishing " + events.size()+" change metadata to track table");
		for(DmlEvent event:events) {
			publish(event);
		}
		return new AsyncResult<String>("success");
	}
	
	public void publish(DmlEvent event) {

		String metadata = extractMetadata(event);
		String changedTableSchema = event.getSchemaName();
		String changedTableName = event.getTableName();
		String type = event.getType().toString();
		Long transactionId = event.getTransactionId();
		PGobject timestamp = getTimestamp(event);
		PGobject oldjson = getJsonOldValues(event);
		PGobject newjson = getJsonNewValues(event);
		
		Object[] params;
		String sql;
		int[] types;
		
		Envelope envelope = event.getEnvelope();
		if (! envelope.isNull()) {
			//Transform Bounding Box of the change into WKB
			GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), epsgCode);
			WKBWriter wkbWriter = new WKBWriter(2, true);
			byte[] wkb = wkbWriter.write(geomFactory.toGeometry(envelope));
			params = new Object[]{wkb, type, changedTableSchema, changedTableName, transactionId, timestamp, metadata, oldjson, newjson};
			types = new int[] {Types.BINARY, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.OTHER, Types.VARCHAR, Types.OTHER, Types.OTHER};
			sql = "INSERT INTO "+schemaname + "." + tableName + "(region, type, schemaname, tablename, transactionId, timestamp, metadata, oldvalues, newvalues) VALUES (?,?,?,?,?,?,?,?,?)";
		}
		else {
			//geometry is null, do not include it in SQL insert statement
			params = new Object[]{type, changedTableSchema, changedTableName, transactionId, timestamp, metadata, oldjson, newjson};
			types = new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.OTHER, Types.VARCHAR, Types.OTHER, Types.OTHER};
			sql = "INSERT INTO "+schemaname + "." + tableName + "(type, schemaname, tablename, transactionId, timestamp, metadata, oldvalues, newvalues) VALUES (?,?,?,?,?,?,?,?)";
			
		}
		template.update(sql, params,types);
	}
	
	private String extractMetadata(DmlEvent event) {
		switch(event.getType()) {
			case delete:
			{
				return extractMetadata(event.getOldValues());
			}
			default:
			{
				return extractMetadata(event.getNewValues());
			}
		}
	}
	
	
	private String extractMetadata(Collection<Cell> cells) {
		List<String> parts = new ArrayList<String>();
		for (Cell cell:cells) {
			for (String pattern:metaInfoSearchPatterns) {
			if (cell.getName().startsWith(pattern) || cell.getName().endsWith(pattern)) {
				parts.add(new String(cell.getName() + ": " + cell.getValue()));
				}
			}
		}
		return Joiner.on(", ").join(parts);
	}
	
	
	private PGobject getJsonOldValues(DmlEvent event) {
		if ((event.getType() == DmlEvent.Type.delete) || (event.getType() == DmlEvent.Type.update)) {
			return getJsonObject(event.getOldValues());
		}
		else {
			return null;
		}
	}
	
	private PGobject getJsonNewValues(DmlEvent event) {
		if ((event.getType() == DmlEvent.Type.insert) || (event.getType() == DmlEvent.Type.update)) {
			return getJsonObject(event.getNewValues());
		}
		else {
			return null;
		}
	}

	private PGobject getJsonObject(List<Cell> cells) {
		List<String> parts = new ArrayList<String>();
		for (Cell cell:cells) {
			parts.add(cell.getJson());
		}
		PGobject pgobject = new PGobject();
		pgobject.setType("jsonb");
		try {
			pgobject.setValue("{" + Joiner.on(", ").join(parts) + "}");
		} catch (SQLException e) {
			log.error("Error while setting JSONB of changed Objects into SQL PGobject type:", e);
		}
		return pgobject;
	}
	
	private PGobject getTimestamp(DmlEvent event) {
		PGobject timestamp = new PGobject();
		timestamp.setType("timestamp");
		try {
			timestamp.setValue( event.getCommitTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
		} catch (SQLException e) {
			log.error("Error while setting Timestamp SQL PGobject type:", e);
		}
		return timestamp;
	}
}
