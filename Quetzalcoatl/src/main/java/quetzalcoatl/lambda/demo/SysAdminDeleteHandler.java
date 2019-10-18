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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import quetzalcoatl.db.ScheduleDAO;
import quetzalcoatl.model.Schedule;

public class SysAdminDeleteHandler implements RequestStreamHandler {
public LambdaLogger logger = null;

	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	int deleteSchedules(int oldest) throws Exception {
		if (logger != null) { logger.log("in deleteAdminSchedule"); }
		ScheduleDAO dao = new ScheduleDAO();
		
		Date date = new Date(System.currentTimeMillis() - oldest * 24 * 60  * 60 * 1000);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.log("yoyoyoy:"+dateFormat.format(date));
		return dao.deleteAdminSchedule(dateFormat.format(date));
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to delete schedules as admin");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    SysAdminDeleteResponse response = null;
	    
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
	    		response = new SysAdminDeleteResponse("options", 200, 0);
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
	    	response = new SysAdminDeleteResponse("Bad Request:" + pe.getMessage(), 422,0);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	SysAdminDeleteRequest req = new Gson().fromJson(body, SysAdminDeleteRequest.class);
	    	logger.log(req.toString());
	    	
	    	SysAdminDeleteResponse resp;
	    	try {
	    		int deleted = deleteSchedules(req.oldDate);
	    		resp = new SysAdminDeleteResponse("Deleted", 200, deleted);
	    	} catch (Exception e) {
	    		resp = new SysAdminDeleteResponse("error", 403, 0);
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
