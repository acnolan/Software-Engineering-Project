package quetzalcoatl.lambda.demo;

public class OrganizerGetScheduleRequest {
	
	public final int id2;

	public OrganizerGetScheduleRequest(int id2) 
		{this.id2 = id2;}
	
	public String toString() {
		return "Get(" + id2 + ")";
	}
}
