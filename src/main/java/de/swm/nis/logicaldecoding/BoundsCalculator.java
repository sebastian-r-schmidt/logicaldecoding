package de.swm.nis.logicaldecoding;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;

import de.swm.nis.logicaldecoding.parser.Cell.Type;
import de.swm.nis.logicaldecoding.parser.Cell;
import de.swm.nis.logicaldecoding.parser.Row;

/**
 * Calculates the Minimum bounding Rectangle (Bounding Box) for a
 * single Row or a List of Row Objects.
 * @author Schmidt.Sebastian2
 *
 */
@Component
public class BoundsCalculator {
	
	private static final Logger log = LoggerFactory.getLogger(BoundsCalculator.class);

	public Envelope findAffectedRegion(Row row) throws ParseException {
		
		Envelope envelope = new Envelope();
		for (Cell cell:row.getOldValues()) {
			if (cell.getType().equals(Type.geometry)) {
				if (!cell.getValue().equals("null")) {
					Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
					envelope.expandToInclude(geom.getEnvelopeInternal());
				}
			}
		}
		for (Cell cell:row.getNewValues()) {
			if (cell.getType().equals(Type.geometry)) {
				if (!cell.getValue().equals("null")) {
					Geometry geom = new WKBReader().read(WKBReader.hexToBytes(cell.getValue()));
					envelope.expandToInclude(geom.getEnvelopeInternal());
				}
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
					log.warn("error on parsing WKB", e);
					throw new RuntimeException("error on parsing WKB", e);
				}
			}
		}
		return envelopes;
	}
	
}
