package quetzalcoatl.lambda.demo;

public class LoginResponse {
	public final int userId;
	public final int httpCode;
	public final String username;
	public final int isAdmin;

	public LoginResponse(int userId, int code, String username, int isAdmin){
		this.userId = userId;
		this.httpCode = code;
		this.username = username;
		this.isAdmin = isAdmin;
	}
	
	public String toString() {
		return "Responsed";
	}
}
