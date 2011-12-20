package bershika.route.googleservice;

public class GeoLocation {
	public float lat;
	public float lng;

	public GeoLocation() {
	}

	public GeoLocation(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "GeoLocation [lat=" + lat + ", lng=" + lng + "]";
	}

}
