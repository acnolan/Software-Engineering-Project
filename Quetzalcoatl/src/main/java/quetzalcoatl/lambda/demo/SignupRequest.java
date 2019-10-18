package quetzalcoatl.lambda.demo;

import java.util.Date;

public class SignupRequest {
	String username;
	String password;
	
	public SignupRequest (String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String toString() {
		return "Create user";
	}
}
