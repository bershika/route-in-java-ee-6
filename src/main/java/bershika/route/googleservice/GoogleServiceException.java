package bershika.route.googleservice;

public class OverQueryLimitException extends Exception {
	private String status;

	public OverQueryLimitException(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
