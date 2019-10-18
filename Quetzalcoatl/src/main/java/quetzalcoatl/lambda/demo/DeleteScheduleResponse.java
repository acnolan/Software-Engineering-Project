package quetzalcoatl.lambda.demo;

import java.util.List;



public class DeleteScheduleResponse {

	public final String errorMessage;
	public final int httpCode;

	public DeleteScheduleResponse(String s, int code)
	{
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}

}
