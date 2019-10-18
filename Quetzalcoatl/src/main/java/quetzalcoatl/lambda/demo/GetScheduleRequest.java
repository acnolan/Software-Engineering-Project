package quetzalcoatl.lambda.demo;

public class GetScheduleRequest {
	public final int id2;
	public final String startTime;
	public final String endTime;
	
	public GetScheduleRequest(int id2, String start, String end) {
		this.id2 = id2;
		this.startTime = start;
		this.endTime = end;
	}
	
	public String toString() {
		return "Get(" + id2 + startTime + endTime + ")";
	}

}
