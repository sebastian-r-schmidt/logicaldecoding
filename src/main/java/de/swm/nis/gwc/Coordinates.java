package de.swm.nis.gwc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Coordinates {

	@JsonProperty("double")
	private double[] values;
	
	public Coordinates() {
		
	}
	
	public Coordinates(double xmin, double ymin, double xmax, double ymax) {
		values = new double[]{xmin,ymin,xmax,ymax};
	}

	public double[] getValues() {
		return values;
	}

	public void setValues(double[] values) {
		this.values = values;
	}
	
}
