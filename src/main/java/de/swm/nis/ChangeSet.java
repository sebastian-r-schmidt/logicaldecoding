package de.swm.nis;

public class ChangeSet {

	private String location;
	private long transactionId;
	private String data;

	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ChangeSet [location=" + location + ", transactionId=" + transactionId + ", data=" + data + "]";
	}
	
}
