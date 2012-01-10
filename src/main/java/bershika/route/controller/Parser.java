package bershika.route.controller;

import java.util.Scanner;
import java.util.Vector;

public class Parser {
	private static Scanner SCAN;

	public static Vector<String> getLines(String input) {
		SCAN = new Scanner(input);
		String line;
		Vector<String> lines = new Vector<String>();
		while (SCAN.hasNextLine()) {
			line = SCAN.nextLine();
			lines.add(line);
		}
		return lines;

	}

	public static String getCity(String input) {
		String city = "";
		String[] tokens = input.split(",");
		if (tokens.length > 1) {
			String[] names = tokens[0].trim().split(" ");
			for(String name: names){
			city += (name.substring(0, 1).toUpperCase()
					+ name.substring(1).toLowerCase() + " ");
			}
		}
		return city.trim();
	}

	public static String getState(String input) {
		String state = "";
		String[] tokens = input.split(",");
		if (tokens.length > 1) {
			state = tokens[1].trim();
			state = state.substring(0, 2).toUpperCase();
		}
		return state;
	}

	public static float getPointRate(String input) {
		String rateStr = "";
		float rate = 0;
		String[] tokens = input.split(",");
		if (tokens.length > 2) {
			rateStr = tokens[2].trim();
			try {
				rate = Float.valueOf(rateStr);
			} catch (NumberFormatException e) {
			}
		}
		return rate;
	}

}
