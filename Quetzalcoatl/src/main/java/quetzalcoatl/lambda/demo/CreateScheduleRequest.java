package quetzalcoatl.lambda.demo;

import java.util.Date;

public class CreateScheduleRequest {
	int organizerId;
	int startTime;
	int endTime;
	int slotLength;
	Date startDate;
	Date endDate;
	String Id;
	int id2;
	String name;
	
public CreateScheduleRequest (int organizerId, int startTime, int endTime, int slotLength, Date startDate, Date endDate, String name) {

		this.organizerId = organizerId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.slotLength=slotLength;
		this.Id = "test";
		this.id2 = id2;
		this.name = name;
	}
	
	public String toString() {
		return "Create(" + organizerId + "," + startTime + ","+endTime+","+slotLength+","+startDate+","+endDate+","+Id+")";
	}
}
