package de.swm.nis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
		Row row = parser.parseLogLine("table tmp.landkreis_neu: INSERT: alkis_id[text]:''LANDSBERG'' beschriftung_pos[geometry]:null datenquelle[integer]:3 erfasst_am[date]:''2015-11-05'' erfasst_durch[text]:''Schmidt.Sebastian2'' the_geom[geometry]:''0106000020EC7A0000010000000103000000010000000C0000003B84A88392C750411155771E44765441900A7B95C9C4504109F537F0098C544156B5B1BDAFD050412B43E90B9F925441B01595649CDE5041DDD9BEDBDF925441411B22CC85EB5041978D3C8D989054410B76780B89EC5041E8A686D4748554419CBE9CBF34E65041031B275B547B54412A9A38A985D75041AC27CC7EC2755441450ED92F65CD504173159AF36A6E5441F53726354BC55041EF06C602AF6F544113194F8685C3504118721F00BC7354413B84A88392C750411155771E44765441'' geaendert_am[date]:null geaendert_durch[text]:null landkreisschluessel[integer]:1889 name[text]:''Landsberg am Lech'' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:''LL''"); 
		System.out.println(row);
		assertNotNull(row);
	}
	
	@Test
	public void testSplitKeyValuePairs() {
		List<String> tokenList = parser.splitKeyValuePairs("landkreisschluessel[integer]:1889 name[text]:''Landsberg am Lechtal'' landkreis_id[integer]:35 _version[integer]:4854754 kuerzel[text]:''LL''");
		assertNotNull(tokenList);
	}
	
	
}
