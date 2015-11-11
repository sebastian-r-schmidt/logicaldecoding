package de.swm.nis.logicaldecoding.gwc;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.vividsolutions.jts.geom.Envelope;

import de.swm.nis.logicaldecoding.gwc.seed.Bounds;
import de.swm.nis.logicaldecoding.gwc.seed.Coordinates;
import de.swm.nis.logicaldecoding.gwc.seed.GwcSeedDAO;
import de.swm.nis.logicaldecoding.gwc.seed.SeedRequest;
import de.swm.nis.logicaldecoding.gwc.seed.Srs;



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



	public void postSeedRequest(Envelope envelope) {

		String gwcurl = gwcBaseUrl + "seed/" + layername + ".json";

		Bounds bounds = new Bounds(new Coordinates(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(),
				envelope.getMaxY()));
		Srs srs = new Srs(31468);
		SeedRequest request = new SeedRequest(layername, bounds, srs, zoomStart, zoomStop, imageFormat, operation,
				numThreads);

		HttpEntity<GwcSeedDAO> httpentity = new HttpEntity<GwcSeedDAO>(new GwcSeedDAO(request), createHeaders(
				gwcUserName, gwcPassword));
		template.exchange(gwcurl, HttpMethod.POST, httpentity, String.class);
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
