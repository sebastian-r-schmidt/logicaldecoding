package de.swm.nis.gwc;

import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.vividsolutions.jts.geom.Envelope;



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

	//TODO parmeterize numThreads and image Format
	

	public void postReseed(Envelope envelope) {

		String gwcurl = gwcBaseUrl + "seed/" + layername + ".json";

		Bounds bounds = new Bounds(new Coordinates(envelope.getMinX(), envelope.getMinY(), envelope.getMaxX(),
				envelope.getMaxY()));
		Srs srs = new Srs(31468);
		SeedRequest request = new SeedRequest(layername, bounds, srs, zoomStart, zoomStop, "image/png", operation, 1);
		
		HttpEntity<GwcSeedOperation> httpentity = new HttpEntity<GwcSeedOperation>(new GwcSeedOperation(request), createHeaders(gwcUserName, gwcPassword));

		template.exchange(gwcurl, HttpMethod.POST, httpentity, String.class);
	}


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
