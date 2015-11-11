package de.swm.nis.logicaldecoding;

import java.util.List;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import de.swm.nis.logicaldecoding.dataaccess.ChangeSetDAO;
import de.swm.nis.logicaldecoding.dataaccess.ChangeSetFetcher;

public class ChangeSetFetcherTest {

	
	//TODO setup mocks for database access.
	
	@Test
	public void testFetchChanges() {
		JdbcTemplate mock = new JdbcTemplate();
		ChangeSetFetcher fetcher = new ChangeSetFetcher();
		List<ChangeSetDAO> changes = fetcher.fetch("testslot", 100);
		//TODO Test if data is correctly contained as expected.
		for (ChangeSetDAO change:changes) {
			System.out.println(change);
		}	
	}
	
	@Test
	public void testPeekChanges() {
		//TODO test Method peekChanges
	}
}
