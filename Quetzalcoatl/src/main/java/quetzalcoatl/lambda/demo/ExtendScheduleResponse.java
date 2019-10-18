package quetzalcoatl.lambda.demo;

import java.util.Date;

public class ExtendScheduleResponse {

	public final Date modDate;
	public final String errorMessage;
	public final int httpCode;
	
	public ExtendScheduleResponse(String s, int code, Date inDate) {
		this.modDate = inDate;
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + errorMessage + "Mod Date: " + modDate.toString() + ")";
	}
}
