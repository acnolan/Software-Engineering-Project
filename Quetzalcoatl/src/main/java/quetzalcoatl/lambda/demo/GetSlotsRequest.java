package quetzalcoatl.lambda.demo;

public class GetSlotsRequest {
	public final int id2;
	
	public GetSlotsRequest(int id2) {
		this.id2 = id2;
	}
	
	public String toString() {
		return "Get(" + id2 + ")";
	}
}
