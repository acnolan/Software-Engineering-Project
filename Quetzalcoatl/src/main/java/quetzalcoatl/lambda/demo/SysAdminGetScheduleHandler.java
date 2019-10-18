package quetzalcoatl.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import quetzalcoatl.model.ExtendedSchedule;
import quetzalcoatl.model.Schedule;
import quetzalcoatl.model.Slot;

public class SysAdminGetScheduleHandler implements RequestStreamHandler {
	public LambdaLogger logger = null;

	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	ArrayList<ExtendedSchedule> getSchedules(int oldest) throws Exception {
		if (logger != null) { logger.log("in getSchedule"); }
		ScheduleDAO dao = new ScheduleDAO();
		//this comment does nothing
		//neither does this one
		Date date = new Date(System.currentTimeMillis() - oldest * 60  * 60 * 1000);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		return dao.getAdminSchedule(dateFormat.format(date));//oldest);
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to get schedules as admin");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    SysAdminGetScheduleResponse response = null;
	    
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
	    		response = new SysAdminGetScheduleResponse(null, "options", 200);
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
	    	response = new SysAdminGetScheduleResponse(null, "Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	SysAdminGetScheduleRequest req = new Gson().fromJson(body, SysAdminGetScheduleRequest.class);
	    	logger.log(req.toString());
	    	
	    	SysAdminGetScheduleResponse resp;
	    	try {
	    		ArrayList<ExtendedSchedule> sch = getSchedules(req.searchTime);
	    		resp = new SysAdminGetScheduleResponse(sch, "got", 200);
	    	} catch (Exception e) {
	    		resp = new SysAdminGetScheduleResponse(null, "error", 403);
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
