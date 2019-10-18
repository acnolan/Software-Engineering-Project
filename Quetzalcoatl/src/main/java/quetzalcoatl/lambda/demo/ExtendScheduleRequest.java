package quetzalcoatl.lambda.demo;

import java.util.Date;

public class ExtendScheduleRequest {
	public final int scheduleID;
	public final Date modTime;

	
	public ExtendScheduleRequest(int id2, Date inModTime) {
		this.scheduleID = id2;
		this.modTime = inModTime;
	}
	
	public String toString() {
		return "Extend(" + scheduleID + modTime + ")";
	}
}
