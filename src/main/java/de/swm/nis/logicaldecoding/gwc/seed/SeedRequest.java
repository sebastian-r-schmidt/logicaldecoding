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
