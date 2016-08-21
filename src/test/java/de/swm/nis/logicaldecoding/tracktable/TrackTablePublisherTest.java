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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import de.swm.nis.logicaldecoding.parser.domain.Cell;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;

public class TrackTablePublisherTest {
	
	@Autowired
	private TrackTablePublisher publisher;
	
	@Mock
	JdbcTemplate jdbctemplate;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		publisher = new TrackTablePublisher(jdbctemplate);
		List<String> searchPatterns = new ArrayList<String>();
		searchPatterns.add("_id");
		publisher.setMetaInfoSearchPatterns(searchPatterns);
		publisher.setEpsgCode(3857);
	}
	
	
	@Test
	public void testPublishInsertUpdateDeleteEventWithoutGeom() {
		Collection<DmlEvent> events = new ArrayList<DmlEvent>();
		events.add(createInsertEventwithoutGeom());
		events.add(createUpdateEvent());
		events.add(createDeleteEvent());
		publisher.publish(events);
	}
	
	@Test
	public void testPublishInsertUpdateDeleteEventWithGeom() {
		Collection<DmlEvent> events = new ArrayList<DmlEvent>();
		events.add(createInsertEventwithGeom());
		events.add(createUpdateEvent());
		events.add(createDeleteEvent());
		publisher.publish(events);
	}
	
	@Test
	public void testPublishInsertEventWithPointGeom() {
		Collection<DmlEvent> events = new ArrayList<DmlEvent>();
		events.add(createInsertEventWithPointGeom());
		publisher.publish(events);
	}

	private DmlEvent createInsertEventwithoutGeom() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.insert);
		event.setCommitTime("2000-01-01 01:00:00+01");
		event.getNewValues().add(createTextCell());
		event.getNewValues().add(createIdColumnCell());
		return event;
	}
	
	private DmlEvent createInsertEventwithGeom() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.insert);
		event.setCommitTime("2000-01-01 01:00:00+01");
		event.getNewValues().add(createTextCell());
		event.getNewValues().add(createIdColumnCell());
		event.getNewValues().add(createPolygonGeomCell());
		event.getNewValues().add(createNullGeomCell());
		return event;
	}
	
	private DmlEvent createInsertEventWithPointGeom() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.insert);
		event.setCommitTime("2000-01-01 01:00:00+01");
		event.getNewValues().add(createPointGeomCell());
		return event;
	}
	
	private DmlEvent createUpdateEvent() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.update);
		event.getOldValues().add(createTextCell());
		event.getNewValues().add(createIdColumnCell());
		return event;
	}
	
	private DmlEvent createDeleteEvent() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.delete);
		event.getOldValues().add(createTextCell());
		event.getOldValues().add(createNullGeomCell());
		return event;
	}
	
	private Cell createTextCell() {
		Cell c = new Cell();
		c.setName("text_column");
		c.setType("text");
		c.setValue("testvalue");
		return c;
	}
	
	private Cell createIdColumnCell() {
		Cell c = new Cell();
		c.setName("int_column_id");
		c.setType("integer");
		c.setValue("123");
		return c;
	}
	
	private Cell createPolygonGeomCell() {
		Cell c = new Cell();
		c.setName("poly_geom_column");
		c.setType("geometry");
		c.setValue("0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441");
		return c;
	}
	
	private Cell createNullGeomCell() {
		Cell c = new Cell();
		c.setName("poly_gom_column");
		c.setType("geometry");
		c.setValue("null");
		return c;
	}
	
	private Cell createPointGeomCell() {
		Cell c = new Cell();
		c.setName("point_gom_column");
		c.setType("geometry");
		c.setValue("01010000000000000050D4324100000000882A5141");
		return c;
	}
}
