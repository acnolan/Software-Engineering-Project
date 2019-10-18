package quetzalcoatl.lambda.demo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SysAdminGetScheduleRequest {
	public final int searchTime;
	
	public SysAdminGetScheduleRequest(int searchTime) {
		this.searchTime = searchTime;
	}
	
	public String toString() {
		return "Get(" + searchTime + ")";
	}
}
