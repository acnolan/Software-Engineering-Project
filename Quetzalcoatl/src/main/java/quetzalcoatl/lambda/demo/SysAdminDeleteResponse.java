package quetzalcoatl.lambda.demo;

import java.util.List;



public class SysAdminDeleteResponse {

	public final String errorMessage;
	public final int httpCode;
	public final int rowsUpdated;

	public SysAdminDeleteResponse(String s, int code, int rowsUpdated)
	{
		this.errorMessage = s;
		this.httpCode = code;
		this.rowsUpdated = rowsUpdated;
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}

}
