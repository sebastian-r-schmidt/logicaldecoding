package de.swm.nis.logicaldecoding.gwc.seed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SeedRequest {

	private String name;
	private Bounds bounds;
	private Srs srs;
	private int zoomStart;
	private int zoomStop;
	private String format;
	private String type;
	private int threadCount;
	
	public SeedRequest() {
		
	}
	
	public SeedRequest(String name, Bounds bounds, Srs srs, int zoomStart, int zoomStop, String format, String type, int threadCount) {
		this.name = name;
		this.bounds = bounds;
		this.srs = srs;
		this.zoomStart = zoomStart;
		this.zoomStop = zoomStop;
		this.format = format;
		this.type = type;
		this.threadCount = threadCount;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bounds getBounds() {
		return bounds;
	}
	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}
	public Srs getSrs() {
		return srs;
	}
	public void setSrs(Srs srs) {
		this.srs = srs;
	}
	public int getZoomStart() {
		return zoomStart;
	}
	public void setZoomStart(int zoomStart) {
		this.zoomStart = zoomStart;
	}
	public int getZoomStop() {
		return zoomStop;
	}
	public void setZoomStop(int zoomStop) {
		this.zoomStop = zoomStop;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getThreadCount() {
		return threadCount;
	}
	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}
	
}
