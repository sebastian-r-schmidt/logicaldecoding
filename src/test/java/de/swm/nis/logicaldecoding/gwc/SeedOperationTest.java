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
package de.swm.nis.logicaldecoding.gwc;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.swm.nis.logicaldecoding.gwc.seed.Bounds;
import de.swm.nis.logicaldecoding.gwc.seed.Coordinates;
import de.swm.nis.logicaldecoding.gwc.seed.GwcSeedDAO;
import de.swm.nis.logicaldecoding.gwc.seed.SeedRequest;
import de.swm.nis.logicaldecoding.gwc.seed.Srs;

public class SeedOperationTest {

	
	@Test
	public void testCreateSeedOperation() throws JsonProcessingException {
		
		GwcSeedDAO seedOperation = new GwcSeedDAO();
		
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
