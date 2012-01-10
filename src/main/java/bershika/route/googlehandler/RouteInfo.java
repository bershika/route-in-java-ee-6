package bershika.route.googlehandler;

public class RouteInfo {
	
	private String userFrom;
	private String userTo;
	private String googleFrom;
	private String googleTo;
	private int distanceInMeters;
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getUserTo() {
		return userTo;
	}
	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}
	public String getGoogleFrom() {
		return googleFrom;
	}
	public void setGoogleFrom(String googleFrom) {
		this.googleFrom = googleFrom;
	}
	public String getGoogleTo() {
		return googleTo;
	}
	public void setGoogleTo(String googleTo) {
		this.googleTo = googleTo;
	}
	public int getDistanceInMeters() {
		return distanceInMeters;
	}
	public void setDistanceInMeters(int distanceInMeters) {
		this.distanceInMeters = distanceInMeters;
	}
	@Override
	public String toString() {
		return "RouteInfo [userFrom=" + userFrom + ", userTo=" + userTo + ", googleFrom=" + googleFrom + ", googleTo="
				+ googleTo + ", distanceInMeters=" + distanceInMeters + "]";
	}
}
