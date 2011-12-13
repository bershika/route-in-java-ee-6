package bershika.route.googleservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.net.URLEncoder;

public class GoogleAPI {
	private static final String BASIC_URL = "http://maps.googleapis.com/maps/api/";
	private static final String OUTPUT = "/json";
	private static final char PARAM_START = '?';
	private static final char AND = '&';
	private static final char OR = '|';
	private static final char EQ = '=';
	private static final String SENSOR_FALSE = "sensor=false";
	private static final String ADD = "address";
	private static final String ORIGIN = "origin";
	private static final String ORIGINS = "origins";
	private static final String DEST = "destination";
	private static final String DESTS = "destinations";

	private String getURLString(final API api) {
		return BASIC_URL + api + OUTPUT;
	}

	private String addSingleParam(String url, String param, String value) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (url.endsWith("json"))
			return url + PARAM_START + param + EQ + value;
		else
			return url + AND + param + EQ + value;
	}

	private String addParams(String url, String param, List<String> values) {
		String result = "";
		if (url.endsWith("json"))
			result = url + PARAM_START + param + EQ;
		else
			result = url + AND + param + EQ;
		for (int i = 0; i < values.size(); i++) {
			try {
				String encParam = URLEncoder.encode(values.get(i), "UTF-8");
				if (i != values.size() - 1)
					result += (encParam + OR);
				else
					result += encParam;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			
		}
		return result;
	}

	private String readURL(String urlStr) throws IOException {
		BufferedReader in;
		String output = "";
		URL url = new URL(urlStr);
		URLConnection connection = url.openConnection();
		in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String line;
		while ((line = in.readLine()) != null){
			output += line;
		}
		in.close();
		return output;

	}

	public String getGoogleService(final API api, final Object[] param) throws GoogleServiceParamException, IOException {
		if (api == API.geocode) {
			if (!(param[0] instanceof String)) {
				throw new GoogleServiceParamException();
			}
			String url = getURLString(api);
			url = addSingleParam(url, ADD, (String)param[0]);
			url += AND + SENSOR_FALSE;
			return readURL(url);
		}
		if (api == API.directions) {
			if(param.length != 2){
				throw new GoogleServiceParamException();
			}
			if (!(param[0] instanceof String) || !(param[0] instanceof String)) {
				throw new GoogleServiceParamException();
			}
			String url = getURLString(api);
			url = addSingleParam(url, ORIGIN, (String)param[0]);
			url = addSingleParam(url, DEST, (String)param[1]);
			url += AND + SENSOR_FALSE;
			System.out.println("Requesting " + url);
			return readURL(url);
		}
		if (api == API.distancematrix) {
			if(param.length != 2){
				throw new GoogleServiceParamException();
			}
			if (!(param[0] instanceof List) || !(param[1] instanceof List)) {
				throw new GoogleServiceParamException();
			}
			String url = getURLString(api);
			url = addParams(url, ORIGINS, (List<String>)param[0]);
			url = addParams(url, DESTS, (List<String>)param[1]);
			url += AND + SENSOR_FALSE;
			return readURL(url);
		}
		return "";
	}

	public enum API {
		distancematrix, directions, geocode
		}

}
