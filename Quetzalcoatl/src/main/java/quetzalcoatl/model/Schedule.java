package quetzalcoatl.model;

//import java.sql.Date;
import java.util.Date;

public class Schedule {
	public final int organizerId;
	public final int startTime;
	public final int endTime;
	public final int slotLength;
	public final Date startDate;
	public final Date endDate;
	public final String Id;
	public final int id2;
	public final int realId;
	public final String participationCode;
	public final String name;
	
	
public Schedule(int organizerId, int startTime, int endTime, int slotLength, Date startDate, Date endDate,int id2, int realId, String participationCode, String name) {

		this.organizerId = organizerId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.slotLength=slotLength;
		this.Id = "test"+Math.random();
		this.id2 = id2;
		this.realId = realId;
		this.participationCode = participationCode;
		this.name = name;
	}
}
