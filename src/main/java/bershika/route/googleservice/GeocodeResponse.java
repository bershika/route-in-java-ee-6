package bershika.route.googleservice;
/**
 * @author anna
 *
 */
public class GeocodeResponse {
	public String status;
	public Result[] results;

	public static class Result{
		public String type;
		public String formatted_address;
		public AddressComponent[] address_components;
		public Geometry geometry;
		
	}
	
	public static class AddressComponent{
		public String long_name; 
		public String short_name;
		public String[] types; 
		
	}
	public static class Geometry{
		public GeoLocation location;
		public String location_type;
	}
}
