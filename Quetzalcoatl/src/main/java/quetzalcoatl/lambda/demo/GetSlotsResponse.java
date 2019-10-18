package quetzalcoatl.lambda.demo;

import java.util.ArrayList;
import java.util.List;

import quetzalcoatl.model.Slot;

public class GetSlotsResponse {
	public final List<Slot> slots;
	public final int slotlength;
	public final String errorMessage;
	public final int httpCode;
	
	public GetSlotsResponse(List<Slot> slots, int slotlength, String s, int code) {
		this.slots = slots;
		this.slotlength = slotlength;
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public GetSlotsResponse(List<Slot> slots, int slotlength) {
		this.slots = slots;
		this.slotlength = slotlength;
		this.errorMessage = "Success";
		this.httpCode = 200;
	}
	
	public GetSlotsResponse(String s, int code) {
		this.slots = new ArrayList<Slot>();
		this.slotlength = 0;
		this.errorMessage = s;
		this.httpCode = code;
	}
	
	public String toString() {
		return "Response(" + slots.toString() + slotlength + errorMessage + ")";
	}
}
