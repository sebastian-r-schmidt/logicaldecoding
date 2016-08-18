/*
 * Copyright 2016 SWM Services GmbH
 */

package de.swm.nis.logicaldecoding.tracktable;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TrackTablePublisher.class, TrackTableTestConfiguration.class})
@Configuration
public class TrackTablePublisherTest {
	
	@Autowired
	private TrackTablePublisher publisher;
	
	@Test
	public void testPublishInsertEvent() {
		Collection<DmlEvent> events = new ArrayList<DmlEvent>();
		events.add(createInsertEvent());
//		events.add(createUpdateEvent());
//		events.add(createDeleteEvent());	
		publisher.publish(events);
	}

	private DmlEvent createInsertEvent() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.insert);
		event.setCommitTime("2000-01-01 01:00:00+01");
		return event;
	}
	
	private DmlEvent createUpdateEvent() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.update);
		return event;
	}
	
	private DmlEvent createDeleteEvent() {
		DmlEvent event = new DmlEvent("testschema","testtable",DmlEvent.Type.delete);
		return event;
	}
}
