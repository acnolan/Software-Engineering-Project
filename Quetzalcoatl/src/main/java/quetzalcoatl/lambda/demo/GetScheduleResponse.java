package quetzalcoatl.lambda.demo;

import java.util.ArrayList;
import java.util.List;

import quetzalcoatl.model.ExtendedSlot;
import quetzalcoatl.model.Schedule;
import quetzalcoatl.model.Slot;

public class GetScheduleResponse {

	public final String startdate;
	public final String enddate;
	public final int starttime;
	public final int endtime;
	public final int slotlength;
	public final List<ExtendedSlot> slots;
	public final String errorMessage;
	public final int httpCode;
	public final String participationCode;

	
	public GetScheduleResponse (String s, int code) {
		this.startdate = "";
		this.enddate = "";
		this.starttime = 0;
		this.endtime = 0;
		this.slotlength = 0;
		this.slots = new ArrayList<ExtendedSlot>();
		this.errorMessage = s;
		this.httpCode = code;
		this.participationCode="";
	}
	
	public GetScheduleResponse (Schedule sc, List<ExtendedSlot> list) {
		this.startdate = sc.startDate.toString();
		this.enddate = sc.endDate.toString();
		this.starttime = sc.startTime;
		this.endtime = sc.endTime;
		this.slotlength = sc.slotLength;
		this.slots = list;
		this.httpCode = 200;
		this.errorMessage = "";
		this.participationCode = "'"+sc.participationCode+"'";
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}

}
