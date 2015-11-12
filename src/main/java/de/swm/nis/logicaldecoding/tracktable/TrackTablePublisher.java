package de.swm.nis.logicaldecoding.tracktable;

import java.sql.Types;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBWriter;

import de.swm.nis.logicaldecoding.parser.Cell;
import de.swm.nis.logicaldecoding.parser.Row;

@Repository
public class TrackTablePublisher {
	
	@Autowired
	private JdbcTemplate template;
	
	@Value("${tracktable.schemaname}")
	private String schemaname;
	
	@Value("${tracktable.tablename}")
	private String tableName;
	
	public void publish(Collection<Row> rows) {
		for(Row row:rows) {
			publish(row);
		}
	}
	
	public void publish(Row row) {
		Envelope envelope = row.getEnvelope();
		
		GeometryFactory geomFactory = new GeometryFactory(new PrecisionModel(), 31468);
		WKBWriter wkbWriter = new WKBWriter(2, true);
		byte[] wkb = wkbWriter.write(geomFactory.toGeometry(envelope));
		String metadata = extractMetaData(row);
		String changedTableSchema = row.getSchemaName();
		String changedTableName = Iterables.get(Splitter.on('.').split(row.getTableName()),1);
		String type = row.getType().toString();
		
		Object[] params = new Object[]{wkb, type, changedTableSchema, changedTableName, metadata};
		int[] types = new int[] {Types.BINARY, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR};
		
		String sql = "INSERT INTO "+schemaname + "." + tableName + "(region, type, schema, tablename, metadata) VALUES (?,?,?,?,?)";
		template.update(sql, params,types);
		
	}
	
	private String extractMetaData(Row row) {
		switch(row.getType()) {
			case delete:
			{
				return extractObjectId(row.getOldValues()) + ", " +extractChangeInfos(row.getOldValues());
			}
			default:
			{
				return extractObjectId(row.getNewValues()) + ", " + extractChangeInfos(row.getNewValues());
			}
			
		}
	}
	
	private String extractObjectId(Collection<Cell> cells) {
		for (Cell cell:cells) {
			if (cell.getName().endsWith("_id") && cell.getType().equals(Cell.Type.integer)) {
				return cell.getName() + ": " + cell.getValue();
			}
		}
		return "<ID unknown>";
	}
	
	private String extractChangeInfos(Collection<Cell> cells) {
		StringBuffer changeInfo = new StringBuffer(); 
		for (Cell cell:cells) {
			if (cell.getName().equals("geaendert_am") || cell.getName().equals("erfasst_am")) {
				changeInfo.append(cell.getName() + ": " + cell.getValue() + ", ");
			}
		}
		if (changeInfo.length()== 0) {
			return "<Changedate unknown>";
		}
		return changeInfo.toString();
	}
	
}
