package quetzalcoatl.lambda.demo;

public class ToggleSlotResponse {
	public final String response;
	public final int httpCode;
	
	public ToggleSlotResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public ToggleSlotResponse (String s) {
		this.response = s;
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
