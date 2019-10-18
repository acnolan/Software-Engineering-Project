package quetzalcoatl.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

public class User {

	public final String username;
	public final String name;
	private final String password_hash;
	private final String token;
	
	public User(String username, String name, String password_hash, String token) {
		this.username = username;
		this.name = name;
		this.password_hash = password_hash;
		this.token = token;
	}
	
	public boolean check_password(String password) {
		// Hash the password
		String hashed = sha512(this.username + "!" + password);
		// Compare
		return this.password_hash.equals(hashed);
	}
	
	public String new_token() {
		// Generate a token
		Random rand = new Random();
		String token = sha512(rand.nextInt() + ":" + this.username);
		return token;
	}
	
	public String sha512(String input) {
    	try {
    		MessageDigest md = MessageDigest.getInstance("SHA-512");
    		byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
    		StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
               sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
    	} catch(Exception e) {
    		return null;
    	}
    }
}
