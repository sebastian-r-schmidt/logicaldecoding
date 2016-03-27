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
package de.swm.nis.logicaldecoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.swm.nis.logicaldecoding.parser.AntlrBasedParser;
import de.swm.nis.logicaldecoding.parser.PgParser;
import de.swm.nis.logicaldecoding.parser.domain.Cell;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;
import de.swm.nis.logicaldecoding.parser.domain.Event;
import de.swm.nis.logicaldecoding.parser.domain.TxEvent;

public class LogParserTest {

	private PgParser parser;

	@Before
	public void setup() {
		parser = new AntlrBasedParser();
	}



	@Test
	public void testParseBegin() {
		Event row = parser.parseLogLine("BEGIN 15228819");
		assertTrue(row instanceof TxEvent);
	}



	@Test
	public void testParseCommit() {
		Event row = parser.parseLogLine("COMMIT 15228819");
		assertTrue(row instanceof TxEvent);
	}



	@Test
	public void testParseInsert() {
		Event event = parser
				.parseLogLine("table tmp.landkreis_neu: INSERT: alkis_id[text]:'LANDSBERG' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lech' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		assertEquals(0, dmlEvent.getOldValues().size());
		assertTrue(dmlEvent.getNewValues().size() > 0);
	}

	@Test
	public void testParseInsert2() {
		Event event = parser
				.parseLogLine("table public.allsupportedtypes: INSERT: a_serial[integer]:5 a_numeric[numeric]:null a_real[real]:null a_double[double precision]:null a_char[character]:null a_varchar[character varying]:'text' a_text[text]:'text with blanks and [ ] brackets' a_boolean[boolean]:null a_json[json]:null a_jsonb[jsonb]:null a_date[date]:null a_timestamp[timestamp without time zone]:null a_interval[interval]:null a_tsvector[tsvector]:null a_uuid[uuid]:null a_postgis_geom[geometry]:null");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		assertEquals(0, dmlEvent.getOldValues().size());
		assertTrue(dmlEvent.getNewValues().size() > 0);
	}

	@Test
	public void testParseUpdate() {
		Event event = parser
				.parseLogLine("table tmp.landkreis_neu: UPDATE: old-key: alkis_id[text]:'LANDSBERG' datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lech' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL' new-tuple: alkis_id[text]:'LANDSBERG' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		assertTrue("row must contain old and new tuples", dmlEvent.getOldValues().size() > 0);
		assertTrue("row must contain old and new tuples", dmlEvent.getNewValues().size() > 0);
	}

	@Test
	public void testParseUpdate2() {
		Event event = parser.parseLogLine("table tmp.landkreis_neu: UPDATE: old-key: alkis_id[text]:'DEBYASDF' datenquelle[integer]:3 erfasst_am[date]:'2015-11-11' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A00000100000001030000000100000007000000C262AD677CC5504163ADF3D62E855441C07B7D2BC3CF5041A1A2DD9DE5935441FCF2D8E814E4504122FF966E168A54417E25B80B24DC504127755F9E0F6E544102110AD094C05041E71AB7913A6F5441839B803B1DC05041A4852AED687E5441C262AD677CC5504163ADF3D62E855441' landkreisschluessel[integer]:166 name[text]:'Landsberg am Lech' landkreis_id[integer]:55 kuerzel[text]:'LL' new-tuple: alkis_id[text]:'DEBYASDF' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-11' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000700000046BB834B77B05041E07EF7A05598544183DA47C04FBD50415D7AD5189EA95441FED90825CED950415F8BDF02799D5441C24DC0906BC65041E61E9A7E707A54410000008005A55041000000C03D7D544107F4561F18AB504121572EB78F91544146BB834B77B05041E07EF7A055985441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:166 name[text]:'Landsberg am Lech' landkreis_id[integer]:55 _version[integer]:null kuerzel[text]:'LL'");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		assertTrue("row must contain old and new tuples", dmlEvent.getOldValues().size() > 0);
		assertTrue("row must contain old and new tuples", dmlEvent.getNewValues().size() > 0);
	}


	@Test
	public void testParseDelete() {
		Event event = parser
				.parseLogLine("table tmp.landkreis_neu: DELETE: alkis_id[text]:'LANDSBERG' datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		
		assertEquals(0, dmlEvent.getNewValues().size());
		assertTrue(dmlEvent.getOldValues().size() > 0);
	}

	@Test
	public void testCellNamesWithUnderscores() {
		Event event = parser
				.parseLogLine("table tmp.a: DELETE: landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(event);
		assertNotNull(event);
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		assertEquals(0, dmlEvent.getNewValues().size());
		assertEquals(5, dmlEvent.getOldValues().size());
		Cell cell = dmlEvent.getOldValues().get(1);
		assertEquals("name", cell.getName());
		assertEquals(Cell.Type.text, cell.getType());
		assertEquals("Landsberg am Lechtal", cell.getValue());
		assertEquals("\"name\": \"Landsberg am Lechtal\"", cell.getJson());
	}


	@Test
	public void testCellIntegerDataType() {
		Event event = parser.parseLogLine("table a.a: DELETE: landkreisschluessel[integer]:1889");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("landkreisschluessel", cell.getName());
		assertEquals(Cell.Type.integer, cell.getType());
		assertEquals("1889", cell.getValue());
		assertEquals("\"landkreisschluessel\": 1889", cell.getJson());
	}
	
	@Test
	public void testCellNegativeIntegerDataType() {
		Event event = parser.parseLogLine("table a.a: DELETE: landkreisschluessel[integer]:-1889");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("landkreisschluessel", cell.getName());
		assertEquals(Cell.Type.integer, cell.getType());
		assertEquals("-1889", cell.getValue());
		assertEquals("\"landkreisschluessel\": -1889", cell.getJson());
	}
	
	@Test
	public void testCellTextDataType() {
		Event event = parser.parseLogLine("table a.a: DELETE: name[text]:'Landsberg am Lech'");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("name", cell.getName());
		assertEquals(Cell.Type.text, cell.getType());
		assertEquals("Landsberg am Lech", cell.getValue());
		assertEquals("\"name\": \"Landsberg am Lech\"", cell.getJson());
	}
		
	@Test 
	public void testCellGeometryDataType() {
			
		Event event = parser
				.parseLogLine("table a.a: DELETE: the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441'");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("the_geom", cell.getName());
		assertEquals(Cell.Type.geometry, cell.getType());
		assertEquals("\"the_geom\": \"0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441\"", cell.getJson());
	}
	
	@Test
	public void testCellRealDataType() {
		Event event = parser.parseLogLine("table a.a: DELETE: height[real]:3.7");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("height", cell.getName());
		assertEquals(Cell.Type.real, cell.getType());
		assertEquals("3.7", cell.getValue());
		assertEquals("\"height\": 3.7", cell.getJson());
	}
	
	@Test
	public void testCellBooleanDataType() {
		Event event = parser.parseLogLine("table a.a: DELETE: toggle[boolean]:true");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("toggle", cell.getName());
		assertEquals(Cell.Type.bool, cell.getType());
		assertEquals("true", cell.getValue());
		assertEquals("\"toggle\": true", cell.getJson());
	}
	
	@Test
	public void testCellJsonbType() {
		Event event = parser.parseLogLine("table a.a: DELETE: value[jsonb]:'{\"jobsite_id\": -2.8}'");
		assertTrue(event instanceof DmlEvent);
		DmlEvent dmlEvent = (DmlEvent)event;
		Cell cell = dmlEvent.getOldValues().get(0);
		assertEquals("value", cell.getName());
		assertEquals(Cell.Type.jsonb, cell.getType());
		assertEquals("{\"jobsite_id\": -2.8}", cell.getValue());
		assertEquals("\"value\": {\"jobsite_id\": -2.8}", cell.getJson());
	}
	

}
