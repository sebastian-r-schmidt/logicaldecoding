package de.swm.nis.logicaldecoding.gwc.seed;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Srs {
	
	@JsonProperty("number")
	private int epsgCode;

	public Srs(int epsg_code) {
		this.epsgCode = epsg_code;
	}
	
	public int getNumber() {
		return epsgCode;
	}

	public void setNumber(int number) {
		this.epsgCode = number;
	}

}
