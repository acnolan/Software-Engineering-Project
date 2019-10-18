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
import quetzalcoatl.model.Slot;


public class FilterSlotHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;

	private List<Slot> getSlots(FilterSlotRequest req) throws Exception {
		if (logger != null) { logger.log("in getSlots: " + req.toString()); }
		SlotDAO dao = new SlotDAO();
		
    	String hours = (req.timeslot/60 < 10) ? ("0" + Integer.toString(req.timeslot / 60)) : Integer.toString(req.timeslot / 60);
    	String minutes = (req.timeslot%60 < 10) ? ("0" + Integer.toString(req.timeslot % 60)) : Integer.toString(req.timeslot % 60);
		logger.log("test: " + (hours + ":" + minutes + ":00"));
		return dao.filterSlot(req);
	}

    @SuppressWarnings("unchecked")
	@Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to filter schedules");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    FilterSlotResponse response = null;
	    
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
	    		response = new FilterSlotResponse(null, "options", 200);
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
	    	response = new FilterSlotResponse(null, "Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	FilterSlotRequest req = new Gson().fromJson(body, FilterSlotRequest.class);
	    	logger.log(req.toString());
	    	
	    	FilterSlotResponse resp;
	    	try {
	    		List<Slot> slots = getSlots(req);
	    		resp = new FilterSlotResponse(slots, "got eem", 200);
	    	} catch (Exception e) {
	    		resp = new FilterSlotResponse(null, e.toString(), 403);
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
