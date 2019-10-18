package quetzalcoatl.lambda.demo;

import java.util.List;

import quetzalcoatl.model.Slot;

public class FilterSlotResponse {
	public final List<Slot> slots;
	public final String errorMessage;
	public final int httpCode;
	
	public FilterSlotResponse(List<Slot> slots, String s, int code) {
		this.slots = slots;
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public FilterSlotResponse(List<Slot> slots) {
		this.slots = slots;
		this.errorMessage = "Success";
		this.httpCode = 200;
	}
	
	public String toString() {
		return "Response(" + errorMessage + ")";
	}
}
