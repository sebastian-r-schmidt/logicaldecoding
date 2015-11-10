package de.swm.nis.gwc;


public class Bounds {

	private Coordinates coords;

	public Bounds() {
		
	}
	
	public Bounds(Coordinates coords) {
		this.coords = coords;
	}
	
	public Coordinates getCoords() {
		return coords;
	}

	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}
	
}
