package quetzalcoatl.lambda.demo;

import java.util.List;

import quetzalcoatl.model.ExtendedSchedule;
import quetzalcoatl.model.Schedule;

public class SysAdminGetScheduleResponse {
	public final List<ExtendedSchedule> schedules;
	public final String errorMessage;
	public final int httpCode;
	
	public SysAdminGetScheduleResponse(List<ExtendedSchedule> schedules, String s, int code) {
		this.schedules = schedules;
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}
}
