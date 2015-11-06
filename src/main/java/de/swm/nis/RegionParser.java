package de.swm.nis;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import de.swm.nis.Cell.Type;

public class RegionParser {

	public Envelope findAffectedRegion(Row row) throws ParseException {
		
		Envelope envelope = new Envelope();
		for (Cell cell:row.getOldValues()) {
			if (cell.getType().equals(Type.geometry)) {
				Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
				envelope.expandToInclude(geom.getEnvelopeInternal());
			}
		}
		for (Cell cell:row.getNewValues()) {
			if (cell.getType().equals(Type.geometry)) {
				Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
				envelope.expandToInclude(geom.getEnvelopeInternal());
			}
		}
		return envelope;
	}
	
	
	public List<Envelope> findAffectedRegion(List<Row> rows) {
		List<Envelope> envelopes = new ArrayList<Envelope>();
		for(Row row:rows) {
			if (row !=null) {
				try {
					envelopes.add(findAffectedRegion(row));
				}
				catch(ParseException e) {
					//TODO Error handling
					System.out.println("error on parsing WKB");
					System.out.println(e);
					throw new RuntimeException("error on parsing WKB");
				}
			}
		}
		return envelopes;
	}
	
}
