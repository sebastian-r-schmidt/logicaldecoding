package de.swm.nis.gwc;

public class GwcSeedOperation {
	
	private SeedRequest seedRequest;

	public GwcSeedOperation() {
		
	}
	
	public GwcSeedOperation(SeedRequest request) {
		this.seedRequest = request;
	}
	
	public SeedRequest getSeedRequest() {
		return seedRequest;
	}

	public void setSeedRequest(SeedRequest seedREquest) {
		this.seedRequest = seedREquest;
	}

}
