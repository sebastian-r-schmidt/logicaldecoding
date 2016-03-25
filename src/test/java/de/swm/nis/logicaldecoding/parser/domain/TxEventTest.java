package de.swm.nis.logicaldecoding.parser.domain;

import static org.junit.Assert.assertEquals;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.junit.Test;

public class TxEventTest {

	@Test
	public void testTimestampParsing() {
		TxEvent event = new TxEvent();
		event.setCommitTime("2000-01-01 01:00:00+01");
		ZonedDateTime time = event.getCommitTime();
		assertEquals(2000, time.getYear());
		assertEquals(1, time.getMonthValue());
		assertEquals(1, time.getDayOfMonth());
		assertEquals(1, time.getHour());
		assertEquals(0, time.getMinute());
		assertEquals(0, time.getSecond());
		assertEquals(ZoneOffset.ofHours(1), time.getOffset());
	}
	
	@Test
	public void testTimestampParsing2() {
		TxEvent event = new TxEvent();
		event.setCommitTime("2016-10-03 17:59:18+02");
		ZonedDateTime time = event.getCommitTime();
		assertEquals(2016, time.getYear());
		assertEquals(10, time.getMonthValue());
		assertEquals(3, time.getDayOfMonth());
		assertEquals(17, time.getHour());
		assertEquals(59, time.getMinute());
		assertEquals(18, time.getSecond());
		assertEquals(ZoneOffset.ofHours(2), time.getOffset());
	}
	
}
