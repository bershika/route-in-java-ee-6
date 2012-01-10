package bershika.route.domain;

public class ReportEntry implements Comparable{

	private String hubName;
	private String serviceNotes;
	private int serviceRate;
	private float distance;
	private float drayage;
	
	public String getHubName() {
		return hubName;
	}

	public void setHubName(String hubName) {
		this.hubName = hubName;
	}

	public String getServiceNotes() {
		return serviceNotes;
	}

	public void setServiceNotes(String serviceNotes) {
		this.serviceNotes = serviceNotes;
	}

	public int getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(int serviceRate) {
		this.serviceRate = serviceRate;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getDrayage() {
		return drayage;
	}

	public void setDrayage(float drayage) {
		this.drayage = drayage;
	}

	@Override
	public int compareTo(Object o) {
		if(! (o instanceof ReportEntry))
			throw new ClassCastException();
		ReportEntry other = (ReportEntry)o;
		int rate = Math.round((serviceRate + drayage)  * 100);
		int otherRate = Math.round((other.serviceRate + other.drayage)  * 100);
		return rate - otherRate;
	}
}
