package de.swm.nis.logicaldecoding.gwc.seed;

/**
 * Represents the SeedRequest Datatype inside GeoWebCache
 * @author Schmidt.Sebastian2
 *
 */
public class GwcSeedDAO {
	
	private SeedRequest seedRequest;

	public GwcSeedDAO() {
		
	}
	
	public GwcSeedDAO(SeedRequest request) {
		this.seedRequest = request;
	}
	
	public SeedRequest getSeedRequest() {
		return seedRequest;
	}

	public void setSeedRequest(SeedRequest seedREquest) {
		this.seedRequest = seedREquest;
	}

}
