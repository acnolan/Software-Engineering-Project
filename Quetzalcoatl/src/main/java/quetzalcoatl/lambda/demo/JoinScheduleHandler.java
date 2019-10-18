package quetzalcoatl.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;

import quetzalcoatl.db.ParticipationDAO;
import quetzalcoatl.db.UserDAO;

public class JoinScheduleHandler implements RequestStreamHandler{
public LambdaLogger logger = null;

	
	/** Load from RDS, if it exists
	 * 
	 * @throws Exception
	 */
	void join(int id, String code) throws Exception {
		if (logger != null) { logger.log("trying to login"); }
		ParticipationDAO dao = new ParticipationDAO();
		dao.joinSchedule(id, code);
	}
	
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    	logger = context.getLogger();
    	logger.log("Loading Java Lambda Handler to login");
        
    	JSONObject headerJson = new JSONObject();
    	headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	    
	    JSONObject responseJson = new JSONObject();
	    responseJson.put("headers", headerJson);
	    
	    JoinScheduleResponse response = null;
	    
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
	    		response = new JoinScheduleResponse();
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
	    	response = new JoinScheduleResponse();  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
	    }
	    
	    if (!processed) {
	    	JoinScheduleRequest req = new Gson().fromJson(body,JoinScheduleRequest.class);
	    	logger.log(req.toString());
	    	
	    	JoinScheduleResponse resp;
	    	try {
	    		join(req.id, req.code);
	    		resp = new JoinScheduleResponse();
	    	} catch (Exception e) {
	    		resp = new JoinScheduleResponse();
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
