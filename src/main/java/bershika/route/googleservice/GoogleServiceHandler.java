package bershika.route.googleservice;

import java.io.IOException;

import com.google.gson.Gson;

public class GoogleServiceHandler {

	private static Gson gson = new Gson();
	private static GoogleAPI google = new GoogleAPI();
	
	
	public static GeocodeResponse getGeocoding(String location){
		Object[] param;
		param = new String[1];
		param[0] = location;
		GeocodeResponse response = null;
		try {
			String responseStr = google.getGoogleService(GoogleAPI.API.geocode, param);
			response = gson.fromJson(responseStr, GeocodeResponse.class);
		} catch (GoogleServiceParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	public static DirectionsResponse getDirections(String from, String to) throws GoogleServiceParamException, IOException, OverQueryLimitException{
		Object[] param;
		param = new String[2];
		param[0] = from;
		param[1] = to;
		DirectionsResponse response = null;
			String responseStr = google.getGoogleService(GoogleAPI.API.directions, param);
			response = gson.fromJson(responseStr, DirectionsResponse.class);
		return response;
	}
	
}
