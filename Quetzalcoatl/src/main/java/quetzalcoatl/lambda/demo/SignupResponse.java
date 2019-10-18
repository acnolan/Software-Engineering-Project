package quetzalcoatl.lambda.demo;

public class SignupResponse {
	String response;
	int httpCode;
	String username;
	String password;
	
	public SignupResponse (String s, int code) {
		this.response = s;
		this.httpCode = code;
	}
	
	// 200 means success
	public SignupResponse (String s, int code, String username, String password) {
		this.response = s;
		this.httpCode = code;
		this.username=username;
		this.password=password;
	}
	
	public String toString() {
		return "Response(" + response + ")";
	}
}
