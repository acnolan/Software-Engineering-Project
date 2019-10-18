package quetzalcoatl.lambda.demo;

import java.util.ArrayList;

public class GetAllUsersResponse {
	public final ArrayList<String> names;
	
	public GetAllUsersResponse(ArrayList<String> names) {
		this.names= names;
	}
}
