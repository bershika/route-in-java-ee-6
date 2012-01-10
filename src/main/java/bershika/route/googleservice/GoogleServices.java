package bershika.route.googleservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class GoogleServices {

	private static Gson gson = new Gson();
	
	
	public static GeocodeResponse getGeocoding(String location) throws GoogleServiceParamException, IOException{
		Object[] param;
		param = new String[1];
		param[0] = location;
		GeocodeResponse response = null;
		String responseStr = GoogleAPI.getGoogleService(GoogleAPI.API.geocode, param);
		response = gson.fromJson(responseStr, GeocodeResponse.class);
		return response;
	}
	
	public static DirectionsResponse getDirections(String from, String to) 
			throws GoogleServiceParamException, IOException{
		Object[] param;
		param = new String[2];
		param[0] = from;
		param[1] = to;
		DirectionsResponse response = null;
			String responseStr = GoogleAPI.getGoogleService(GoogleAPI.API.directions, param);
			response = gson.fromJson(responseStr, DirectionsResponse.class);
		return response;
	}
	
	public static DistanceMatrixResponse getDistanceMatrix(List<String> from, List<String> to) 
			throws GoogleServiceParamException, IOException{
		Object[] param;
		param = new Object[2];
		param[0] = from;
		param[1] = to;
		DistanceMatrixResponse response = null;
			String responseStr = GoogleAPI.getGoogleService(GoogleAPI.API.distancematrix, param);
			response = gson.fromJson(responseStr, DistanceMatrixResponse.class);
		return response;
	}
	
	public static List<GeoLocation> decodePoly(String encoded) {
	    List<GeoLocation> poly = new ArrayList<GeoLocation>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        GeoLocation p = new GeoLocation((int) (((double) lat / 1E5) * 1E6),
	             (int) (((double) lng / 1E5) * 1E6));
	        poly.add(p);
	    }

	    return poly;
	}
	
}
