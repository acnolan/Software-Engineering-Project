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

public class TestJoinSchedule {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testJoin() throws IOException {
		JoinScheduleHandler handler = new JoinScheduleHandler();
	 	Date start = new Date();
	 	Date end = new Date();
     	JoinScheduleRequest jr = new JoinScheduleRequest(25, "0.8980433033611408");
     	String joinRequest = new Gson().toJson(jr);
     	String jsonRequest = new Gson().toJson(new PostRequest(joinRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("joinschedule"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	JoinScheduleResponse resp = new Gson().fromJson(post.body, JoinScheduleResponse.class);
     	Assert.assertEquals("wow", resp.wow);
	}
}
