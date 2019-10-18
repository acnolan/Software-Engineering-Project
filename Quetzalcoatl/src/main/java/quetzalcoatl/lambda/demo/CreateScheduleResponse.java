package quetzalcoatl.lambda.demo;

public class CreateScheduleResponse {
	String response;
	int httpCode;
	String id;
	
	public CreateScheduleResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public CreateScheduleResponse (String s, String s2) {
		this.response = s;
		this.httpCode = 200;
		this.id=s2;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
