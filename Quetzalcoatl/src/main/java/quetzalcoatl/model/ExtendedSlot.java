package quetzalcoatl.model;

import java.util.Date;

public class ExtendedSlot {
	public final int id;
	public final int scheduleid;
	public final Date datetime;
	public int bookedby;
	public String username;
	
	public ExtendedSlot(int id, int scheduleid, Date datetime, int bookedby, String username) {
		this.id = id;
		this.scheduleid = scheduleid;
		this.datetime = datetime;
		this.bookedby = bookedby;
		this.username = username;
	}
}
