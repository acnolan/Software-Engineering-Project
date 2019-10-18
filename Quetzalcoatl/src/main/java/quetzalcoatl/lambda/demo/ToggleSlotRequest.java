package quetzalcoatl.lambda.demo;

import java.util.List;

public class ToggleSlotRequest {
	public final boolean isClose;
	public final List<Integer> slotids;
	
	public ToggleSlotRequest(boolean b, List<Integer> s) {
		this.isClose = b;
		this.slotids = s;
	}
	
	public String toString() {
		return "ToggleSlots(" + slotids.toString() + ")";
	}
	
}
