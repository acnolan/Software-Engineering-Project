package quetzalcoatl.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import quetzalcoatl.db.SlotDAO;

public class ToggleSlotHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	void switchSlots(boolean isClose, List<Integer> los) throws Exception {
		if (logger != null) { logger.log("in toggleSlot"); }
		SlotDAO dao = new SlotDAO();
		
		dao.switchAllSlots(isClose, los);
	}
	
    @SuppressWarnings("unchecked")
	@Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to toggle a slot");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    
	    ToggleSlotResponse response = null;
	    
	    // extract body from incoming HTTP POST request. If error return 422
	    String body;
	    boolean processed = false;
	    try {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    	JSONParser parser = new JSONParser();
	    	JSONObject event = (JSONObject) parser.parse(reader);
	    	logger.log("event: " + event.toJSONString());
	    	
	    	body = (String)event.get("body");
    		if (body == null) {
    			body = event.toJSONString(); // for testing
    		}
	    } catch (ParseException pe) {
	    	logger.log(pe.toString());
	    	response = new ToggleSlotResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	ToggleSlotRequest req = new Gson().fromJson(body, ToggleSlotRequest.class);
	    	logger.log(req.toString());
	    	
	    	ToggleSlotResponse resp;
	    	try {
	    		switchSlots(req.isClose, req.slotids);
	    		resp = new ToggleSlotResponse("Slots successfully opened/closed");
	    	} catch (Exception e) {
	    		resp = new ToggleSlotResponse("error, slotDAO exception thrown: " + e.toString() , 403);
	    	}
	    	
	    	responseJson.put("body", new Gson().toJson(resp));	
	    }
	    
	    logger.log("end result:" + responseJson.toJSONString());
	    logger.log(responseJson.toJSONString());
	    OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	    writer.write(responseJson.toJSONString());
	    writer.close();
    }

}
