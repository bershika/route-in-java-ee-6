package bershika.route.googleservice;

public class DirectionsResponse {
	public String status;
	public Route[]  routes;
	public static class Route{
		public String summary;
		public Leg[] legs;
		public Polyline overview_polyline;
	}
	public static class Leg{
      public Distance distance;
      public GeoLocation start_location;
      public GeoLocation end_location;
      public String start_address;
      public String end_address;
	}

	public static class Distance{
		public int value;
	}
	
	public static class Polyline{
		public String points;
	}
}
