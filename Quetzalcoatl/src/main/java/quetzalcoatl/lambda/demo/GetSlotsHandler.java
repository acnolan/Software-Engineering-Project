package quetzalcoatl.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import quetzalcoatl.db.ScheduleDAO;
import quetzalcoatl.db.SlotDAO;
import quetzalcoatl.model.Schedule;
import quetzalcoatl.model.Slot;

public class GetSlotsHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	List<Slot> getSlots(int id2) throws Exception {
		if (logger != null) { logger.log("in getSlots"); }
		SlotDAO dao = new SlotDAO();
		
		return dao.getScheduleSlots(id2);
	}
	
	int getSlotLength(int id2) throws Exception {
		if (logger != null) { logger.log("in getSlots"); }
		ScheduleDAO dao = new ScheduleDAO();
		
		return dao.getSlotLength(id2);
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to get a list of all slots in a schedule");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    GetSlotsResponse response = null;
	    
	    // extract body from incoming HTTP POST request. If error return 422
	    String body;
	    boolean processed = false;
	    try {
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	    	JSONParser parser = new JSONParser();
	    	JSONObject event = (JSONObject) parser.parse(reader);
	    	logger.log("event:" + event.toJSONString());
	    	
	    	String method = (String) event.get("httpMethod");
	    	if(method != null && method.equalsIgnoreCase("OPTIONS")) {
	    		logger.log("Options request");
	    		response = new GetSlotsResponse("options", 200);
	    		responseJson.put("body", new Gson().toJson(response));
	    		processed = true;
	    		body = null;
	    	} else {
	    		body = (String)event.get("body");
	    		if (body == null) {
	    			body = event.toJSONString(); // for testing
	    		}
	    	}
	    } catch (ParseException pe) {
	    	logger.log(pe.toString());
	    	response = new GetSlotsResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	GetSlotsRequest req = new Gson().fromJson(body, GetSlotsRequest.class);
	    	logger.log(req.toString());
	    	
	    	try {
	    		List<Slot> slots = getSlots(req.id2);
	    		int slotlength = getSlotLength(req.id2);
	    		response = new GetSlotsResponse(slots, slotlength);
	    	} catch (Exception e) {
	    		response = new GetSlotsResponse("error", 403);
	    	}
	    	
	    	responseJson.put("body", new Gson().toJson(response));	
	    }
	    
	    logger.log("end result:" + responseJson.toJSONString());
	    logger.log(responseJson.toJSONString());
	    OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
	    writer.write(responseJson.toJSONString());
	    writer.close();
    }

}
