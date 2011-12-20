package bershika.route.googleservice;

public class GoogleServiceException extends Exception {
	private String status;

	public GoogleServiceException(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
