package quetzalcoatl.lambda.demo;

public class FilterSlotRequest {
	//filtered by Year, Month, Day-Of-Week, Day-Of-Month, or Timeslot
	public final int id2;
	public final int year;
	public final int month;
	public final int dayOfMonth;
	public final int dayOfWeek;
	public final int timeslot;
	
	public FilterSlotRequest(int id2, int year, int month, int dayOfMonth, int dayOfWeek, int timeslot) {
		this.id2 = id2;
		this.year = year;
		this.month = month;
		this.dayOfMonth = dayOfMonth;
		this.dayOfWeek = dayOfWeek;
		this.timeslot = timeslot;
	}
	
	public String toString() {
		return "Get(" + id2 + year + month + dayOfMonth + dayOfWeek + timeslot + ")";
	}
}
