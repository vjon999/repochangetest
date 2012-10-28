package exception;

public class BaseException extends Exception {
	String message;

	public BaseException(String message) {
		this.message=message;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
