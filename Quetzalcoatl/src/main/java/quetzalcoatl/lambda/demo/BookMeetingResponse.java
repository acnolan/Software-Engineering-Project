package quetzalcoatl.lambda.demo;

public class BookMeetingResponse {

	public final String errorMessage;
	public final int httpCode;

	public BookMeetingResponse(String s, int code)
	{
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}
}
