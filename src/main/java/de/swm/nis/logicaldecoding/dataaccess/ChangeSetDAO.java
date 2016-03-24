package de.swm.nis.logicaldecoding.dataaccess;

/**
 * Data access object for Changesets from PostgreSQL Replication slots.
 * @author Schmidt.Sebastian2
 *
 */
public class ChangeSetDAO {

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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + (int) (transactionId ^ (transactionId >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChangeSetDAO other = (ChangeSetDAO) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (transactionId != other.transactionId)
			return false;
		return true;
	}
	
	
	
}
