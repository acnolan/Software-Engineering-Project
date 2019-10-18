package quetzalcoatl.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import quetzalcoatl.model.PostRequest;
import quetzalcoatl.model.PostResponse;

public class TestGetSchedules {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testGetSchedules() throws IOException {
		GetScheduleHandler handler = new GetScheduleHandler();
	 	Date start = new Date();
	 	Date end = new Date();
     	GetScheduleRequest gr = new GetScheduleRequest(174, "2018-11-25 00:00:00", "2018-11-29 00:00:00");
     	String getScheduleRequest = new Gson().toJson(gr);
     	String jsonRequest = new Gson().toJson(new PostRequest(getScheduleRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("getschedule"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	GetScheduleResponse resp = new Gson().fromJson(post.body, GetScheduleResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
