package quetzalcoatl.lambda.demo;

public class BookMeetingRequest {
	
	public final int slotID;
	public final int participantID;
	
	public BookMeetingRequest(int inSlot,int inParticipant) {
		
		this.slotID = inSlot;
		this.participantID = inParticipant;
	}
	
	public String toString() {
		return "Book(" + slotID + ") for Participant(" + participantID + ")";
	}
}
