package quetzalcoatl.lambda.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysAdminDeleteRequest {
	public final int oldDate;

	public SysAdminDeleteRequest(int searchTime) {

		this.oldDate = searchTime;
	}
	
	public String toString() {
		return "Delete(" + oldDate + ")";
	}
}
