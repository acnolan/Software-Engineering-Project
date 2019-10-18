package quetzalcoatl.model;

import java.util.Date;

public class Slot {

	public final int id;
	public final int scheduleid;
	public final Date datetime;
	public int bookedby;
	
	public Slot(int id, int scheduleid, Date datetime, int bookedby) {
		this.id = id;
		this.scheduleid = scheduleid;
		this.datetime = datetime;
		this.bookedby = bookedby;
	}
}
