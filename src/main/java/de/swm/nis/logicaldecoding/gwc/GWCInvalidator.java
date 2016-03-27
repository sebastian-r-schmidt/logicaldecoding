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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Future;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.vividsolutions.jts.geom.Envelope;

import de.swm.nis.logicaldecoding.gwc.seed.Bounds;
import de.swm.nis.logicaldecoding.gwc.seed.Coordinates;
import de.swm.nis.logicaldecoding.gwc.seed.GwcSeedDAO;
import de.swm.nis.logicaldecoding.gwc.seed.SeedRequest;
import de.swm.nis.logicaldecoding.gwc.seed.Srs;
import de.swm.nis.logicaldecoding.parser.domain.DmlEvent;



/**
 * This class is able to communicate with a GeoWebCache instance (see geoserver.org and geowebcache.org) in order to
 * send a "seed" request (refresh, invalidate or renew) in order to manipuliate the Cache at certain area.
 * 
 * @author Schmidt.Sebastian2
 *
 */
@Component
public class GWCInvalidator {

	private RestTemplate template;

	private static final Logger log = LoggerFactory.getLogger(GWCInvalidator.class);



	public GWCInvalidator() {
		template = new RestTemplate();
	}

	@Value("${gwc.layername}")
	private String layername;

	@Value("${gwc.rest.baseurl}")
	private String gwcBaseUrl;

	@Value("${gwc.username}")
	private String gwcUserName;

	@Value("${gwc.password}")
	private String gwcPassword;

	@Value("${gwc.zoomstart}")
	private int zoomStart;

	@Value("${gwc.zoomstop}")
	private int zoomStop;

	@Value("${gwc.operation}")
	private String operation;

	@Value("${gwc.threadcount}")
	private int numThreads;

	@Value("${gwc.imageformat}")
	private String imageFormat;
	
	@Value("${postgresql.epsgCode}")
	private int epsgCode;



	@Async
	public Future<String> postSeedRequests(Collection<DmlEvent> rows) {

		log.debug("Calculating affected Regions...");
		Collection<Envelope> envelopes = findAffectedRegion(rows);
		log.info("sending " + envelopes.size() + " Seed Requests to geoWebCache...");
		for (Envelope env : envelopes) {
			log.debug("Envelope: " + env.toString());
			postSeedRequest(env);
		}
		return new AsyncResult<String>("Success");
	}



	//TODO find out if we were successfull and catch exceptions on errors (HTTP 500, no connection possible, ....)
	private void postSeedRequest(Envelope envelope) {

		String gwcurl = gwcBaseUrl + "seed/" + layername + ".json";

		Bounds bounds = new Bounds(new Coordinates(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(),
				envelope.getMaxY()));
		Srs srs = new Srs(epsgCode);
		SeedRequest request = new SeedRequest(layername, bounds, srs, zoomStart, zoomStop, imageFormat, operation,
				numThreads);

		HttpEntity<GwcSeedDAO> httpentity = new HttpEntity<GwcSeedDAO>(new GwcSeedDAO(request), createHeaders(
				gwcUserName, gwcPassword));
		ResponseEntity response = template.exchange(gwcurl, HttpMethod.POST, httpentity, String.class);
		log.info("HTTP Return code: " + response.getStatusCode());
	}



	private Collection<Envelope> findAffectedRegion(Collection<DmlEvent> rows) {
		Collection<Envelope> envelopes = new ArrayList<Envelope>();
		for (DmlEvent row : rows) {
			if (row != null) {
				envelopes.add(row.getEnvelope());
			}
		}
		return envelopes;
	}



	@SuppressWarnings("serial")
	private HttpHeaders createHeaders(String username, String password) {
		return new HttpHeaders() {
			{
				String auth = username + ":" + password;
				byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
				String authHeader = "Basic " + new String(encodedAuth);
				set("Authorization", authHeader);
			}
		};
	}

}
