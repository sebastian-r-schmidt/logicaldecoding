package de.swm.nis.logicaldecoding;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import de.swm.nis.logicaldecoding.dataaccess.ChangeSetDAO;
import de.swm.nis.logicaldecoding.dataaccess.ChangeSetFetcher;


public class ChangeSetFetcherTest {

	@Mock
	JdbcTemplate jdbctemplate;



	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}



	@Test
	public void testFetchChanges() {
		List<ChangeSetDAO> changes = new ArrayList<ChangeSetDAO>();
		
		ChangeSetDAO item1 = new ChangeSetDAO();
		item1.setData("dummy data");
		item1.setLocation("X234asf");
		item1.setTransactionId(12345);
		changes.add(item1);
		
		when(jdbctemplate.query(eq("SELECT * from pg_logical_slot_get_changes(?, NULL, ?)"),
						refEq(new Object[] { "testslot", 100 }), Matchers.<RowMapper<ChangeSetDAO>>any())).thenReturn(changes);

		ChangeSetFetcher fetcher = new ChangeSetFetcher(jdbctemplate);
		List<ChangeSetDAO> fetchedChanges = fetcher.fetch("testslot", 100);
		
		assertEquals(changes.size(),fetchedChanges.size());
		assertEquals(changes.size(),1);
		assertEquals(changes.get(0), fetchedChanges.get(0));
	}


	@Test
	public void testPeekChanges() {
		when(
				jdbctemplate.queryForObject(eq("SELECT count(*) from pg_logical_slot_peek_changes(?, NULL, NULL)"),
						refEq(new Object[] { "testslot" }), eq(Long.class))).thenReturn(100L);

		ChangeSetFetcher fetcher = new ChangeSetFetcher(jdbctemplate);
		Long numChanges = fetcher.peek("testslot");
		assertEquals("Expected a num of 100 available changes", numChanges, (Long) 100L);

		
	}
}
