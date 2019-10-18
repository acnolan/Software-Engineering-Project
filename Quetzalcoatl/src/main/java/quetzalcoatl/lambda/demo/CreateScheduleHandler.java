package quetzalcoatl.lambda.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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

/**
 * Found gson JAR file from
 * https://repo1.maven.org/maven2/com/google/code/gson/gson/2.6.2/gson-2.6.2.jar
 */
public class CreateScheduleHandler implements RequestStreamHandler {

	public LambdaLogger logger = null;

	/** Load from RDS, if it exists
	 * 
	 * @throws Exception 
	 */
  int createSchedule(int organizerId, int startTime, int endTime, int slotLength, Date startDate, Date endDate,int id2, String name) throws Exception {

		if (logger != null) { logger.log("in createSchedule"); }
		ScheduleDAO dao = new ScheduleDAO();
		
		// check if present
		//Schedule exist = dao.getSchedule(Id);
		Schedule schedule = new Schedule(organizerId, startTime, endTime, slotLength, startDate, endDate, id2,0,"",name);
		//if (exist == null) {
			return dao.addSchedule(schedule);
		//} else {
		//	return dao.updateSchedule(schedule);
		//}
	}
	
	int createSlotsForSchedule(int scheduleid, int startTime, int endTime, int slotLength, Date startDate, Date endDate) throws Exception {
		System.out.println("Creating slots for schedule " + scheduleid);
		// Get number of slots
		int slotCount = (endTime - startTime) / slotLength;
		// Insertion counter
		int icount = 0;
		// Slot object
		SlotDAO sdao = new SlotDAO();
		// Iterate over days
		for(Date i = startDate; i.before(endDate) || i.equals(endDate); i = new Date(i.getTime() + TimeUnit.DAYS.toMillis(1))) {
			// Iterate over slots
			for(int s = 0;s < slotCount; s++) {
				// Create the slot
				Slot slot = new Slot(0,scheduleid,new Date(i.getTime() + TimeUnit.MINUTES.toMillis(startTime + (s * slotLength))), 0);
				// Insert the slot
				sdao.insertSlot(slot);
				icount++;
			}
		}
		// If it hasn't crashed by now it's probably okay
		return icount;
}
	
	@Override
	public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
		logger = context.getLogger();
		logger.log("Loading Java Lambda handler to create constant");

		JSONObject headerJson = new JSONObject();
		headerJson.put("Content-Type",  "application/json");  // not sure if needed anymore?
		headerJson.put("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
	    headerJson.put("Access-Control-Allow-Origin",  "*");
	        
		JSONObject responseJson = new JSONObject();
		responseJson.put("headers", headerJson);

		CreateScheduleResponse response = null;
		
		// extract body from incoming HTTP POST request. If any error, then return 422 error
		String body;
		boolean processed = false;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			JSONParser parser = new JSONParser();
			JSONObject event = (JSONObject) parser.parse(reader);
			logger.log("event:" + event.toJSONString());
			
			String method = (String) event.get("httpMethod");
			if (method != null && method.equalsIgnoreCase("OPTIONS")) {
				logger.log("Options request");
				response = new CreateScheduleResponse("name", 200);  // OPTIONS needs a 200 response
		        responseJson.put("body", new Gson().toJson(response));
		        processed = true;
		        body = null;
			} else {
				body = (String)event.get("body");
				if (body == null) {
					body = event.toJSONString();  // this is only here to make testing easier
				}
			}
		} catch (ParseException pe) {
			logger.log(pe.toString());
			response = new CreateScheduleResponse("Bad Request:" + pe.getMessage(), 422);  // unable to process input
	        responseJson.put("body", new Gson().toJson(response));
	        processed = true;
	        body = null;
		}

		if (!processed) {
			CreateScheduleRequest req = new Gson().fromJson(body, CreateScheduleRequest.class);
			logger.log(req.toString());
			logger.log("this is words: " + req.Id);
			CreateScheduleResponse resp;
			try {
				int csr = createSchedule(req.organizerId, req.startTime, req.endTime, req.slotLength, req.startDate, req.endDate, req.id2, req.name);
				if (csr > 0) {
					int c = createSlotsForSchedule(csr, req.startTime, req.endTime, req.slotLength, req.startDate, req.endDate);
					resp = new CreateScheduleResponse("Successfully created schedule: " + csr + " " + c, 200);
				} else {
					resp = new CreateScheduleResponse("Unable to create schedule: " + csr, 422);
				}
				// Create the slots
				//createSlotsForSchedule(csr, req.startTime, req.endTime, req.slotLength, req.startDate, req.endDate);
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
	        	PrintWriter pw = new PrintWriter(sw);
	        	e.printStackTrace(pw);
	        	String sStackTrace = sw.toString(); // stack trace as a string
				resp = new CreateScheduleResponse("Unable to create schedule: " + req.Id + "(" + e.getMessage() + ") [" + sStackTrace + "]", 403);
			}
			
			// compute proper response
	        responseJson.put("body", new Gson().toJson(resp));  
		}
		
        logger.log("end result:" + responseJson.toJSONString());
        logger.log(responseJson.toJSONString());
        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(responseJson.toJSONString());  
        writer.close();
	}
}
