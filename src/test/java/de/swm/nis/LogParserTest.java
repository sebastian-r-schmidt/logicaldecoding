package de.swm.nis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;



//@RunWith(SpringJUnit4ClassRunner.class)
public class LogParserTest {

	private LogParser parser;



	@Before
	public void setup() {
		parser = new LogParser();
	}



	@Test
	public void testParseBegin() {
		Row row = parser.parseLogLine("BEGIN 15228819");
		assertNull(row);
	}



	@Test
	public void testParseCommit() {
		Row row = parser.parseLogLine("COMMIT 15228819");
		assertNull(row);
	}



	@Test
	public void testParseInsert() {
		Row row = parser
				.parseLogLine("table tmp.landkreis_neu: INSERT: alkis_id[text]:'LANDSBERG' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lech' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(row);
		assertNotNull(row);
		assertEquals(0, row.getOldValues().size());
		assertTrue(row.getNewValues().size() > 0);
	}



	@Test
	public void testParseUpdate() {
		Row row = parser
				.parseLogLine("table tmp.landkreis_neu: UPDATE: old-key: alkis_id[text]:'LANDSBERG' datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lech' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL' new-tuple: alkis_id[text]:'LANDSBERG' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(row);
		assertNotNull(row);
		assertTrue("row must contain old and new tuples", row.getOldValues().size() > 0);
		assertTrue("row must contain old and new tuples", row.getNewValues().size() > 0);
	}

	@Test
	public void testParseUpdate2() {
		Row row = parser.parseLogLine("table tmp.landkreis_neu: UPDATE: old-key: alkis_id[text]:'DEBYASDF' datenquelle[integer]:3 erfasst_am[date]:'2015-11-11' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A00000100000001030000000100000007000000C262AD677CC5504163ADF3D62E855441C07B7D2BC3CF5041A1A2DD9DE5935441FCF2D8E814E4504122FF966E168A54417E25B80B24DC504127755F9E0F6E544102110AD094C05041E71AB7913A6F5441839B803B1DC05041A4852AED687E5441C262AD677CC5504163ADF3D62E855441' landkreisschluessel[integer]:166 name[text]:'Landsberg am Lech' landkreis_id[integer]:55 kuerzel[text]:'LL' new-tuple: alkis_id[text]:'DEBYASDF' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:'2015-11-11' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000700000046BB834B77B05041E07EF7A05598544183DA47C04FBD50415D7AD5189EA95441FED90825CED950415F8BDF02799D5441C24DC0906BC65041E61E9A7E707A54410000008005A55041000000C03D7D544107F4561F18AB504121572EB78F91544146BB834B77B05041E07EF7A055985441' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:166 name[text]:'Landsberg am Lech' landkreis_id[integer]:55 _version[integer]:null kuerzel[text]:'LL'");
		System.out.println(row);
		assertNotNull(row);
		assertTrue("row must contain old and new tuples", row.getOldValues().size() > 0);
		assertTrue("row must contain old and new tuples", row.getNewValues().size() > 0);
	}


	@Test
	public void testParseDelete() {
		Row row = parser
				.parseLogLine("table tmp.landkreis_neu: DELETE: alkis_id[text]:'LANDSBERG' datenquelle[integer]:3 erfasst_am[date]:'2015-11-05' erfasst_durch[text]:'Schmidt.Sebastian2' the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441' landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		System.out.println(row);
		assertNotNull(row);
		assertEquals(0, row.getNewValues().size());
		assertTrue(row.getOldValues().size() > 0);
	}



	@Test
	public void testSplitKeyValuePairs() {
		List<String> tokenList = parser
				.splitKeyValuePairs("landkreisschluessel[integer]:1889 name[text]:'Landsberg am Lechtal' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:'LL'");
		assertNotNull(tokenList);
	}


	@Test
	public void testParseCell() {
		Cell cell = parser.parseCell("landkreisschluessel[integer]:1889");
		assertEquals("landkreisschluessel", cell.getName());
		assertEquals(Cell.Type.integer, cell.getType());
		assertEquals("1889", cell.getValue());

		cell = parser.parseCell("name[text]:'Landsberg am Lech'");
		assertEquals("name", cell.getName());
		assertEquals(Cell.Type.text, cell.getType());
		assertEquals("Landsberg am Lech", cell.getValue());

		cell = parser
				.parseCell("the_geom[geometry]:'0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441'");
		assertEquals("the_geom", cell.getName());
		assertEquals(Cell.Type.geometry, cell.getType());

		cell = parser.parseCell("height[real]:3.7");
		assertEquals("height", cell.getName());
		assertEquals(Cell.Type.real, cell.getType());
		assertEquals("3.7", cell.getValue());
	}

}
