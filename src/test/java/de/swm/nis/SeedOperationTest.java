package de.swm.nis;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.swm.nis.gwc.Bounds;
import de.swm.nis.gwc.Coordinates;
import de.swm.nis.gwc.GwcSeedOperation;
import de.swm.nis.gwc.SeedRequest;
import de.swm.nis.gwc.Srs;

public class SeedOperationTest {

	
	@Test
	public void testCreateSeedOperation() throws JsonProcessingException {
		
		GwcSeedOperation seedOperation = new GwcSeedOperation();
		
		SeedRequest seed = new SeedRequest();
		seed.setName("testlayer");
		seed.setSrs(new Srs(31468));
		seed.setThreadCount(2);
		seed.setType("reseed");
		seed.setFormat("image/png");
		seed.setZoomStart(0);
		seed.setZoomStop(12);
		
		Coordinates coords = new Coordinates();
		coords.setValues(new double[]{1.0,2.0,3.0,4.0});
		
		Bounds bounds = new Bounds();
		bounds.setCoords(coords);
		
		seed.setBounds(bounds);
		
		seedOperation.setSeedRequest(seed);
		
		ObjectMapper mapper = new ObjectMapper();
		String result = mapper.writeValueAsString(seedOperation);
		System.out.println(result);
		
	}
}
