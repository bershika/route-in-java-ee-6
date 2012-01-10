package bershika.route.googleservice;

import java.util.List;

public class DistanceMatrixResponse {
	public String status;
	public List<String> origin_addresses;
	public List<String> destination_addresses;
	public List<Row> rows;

	public static class Row {
		public List<Element> elements;

		@Override
		public String toString() {
			return "Row [elements=" + elements + "]";
		}
		
	}

	public static class Element {
		public String status;
		public Distance distance;
		
	}

	@Override
	public String toString() {
		return "DistanceMatrixResponse [status=" + status + ", origin_addresses=" + origin_addresses
				+ ", destination_addresses=" + destination_addresses + ", rows=" + rows + "]";
	}
	
}
