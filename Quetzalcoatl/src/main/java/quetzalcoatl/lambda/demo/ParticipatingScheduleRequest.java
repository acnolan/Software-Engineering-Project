package quetzalcoatl.lambda.demo;

public class ParticipatingScheduleRequest {
	public final int userId;

	public ParticipatingScheduleRequest(int userId) {
		this.userId = userId;
	}
	
	public String toString() {
		return "Get(" + userId + ")";
	}
}
