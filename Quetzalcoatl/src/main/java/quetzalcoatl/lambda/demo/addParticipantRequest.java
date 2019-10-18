package quetzalcoatl.lambda.demo;

public class addParticipantRequest {
	public final String name;
	public final int schId;
	
	public addParticipantRequest(String name, int schId) {
		this.name = name;
		this.schId = schId;
	}
}
