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

public class TestOrganizerGet {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testOrganizerGet() throws IOException {
		OrganizerGetScheduleHandler handler = new OrganizerGetScheduleHandler();
     	OrganizerGetScheduleRequest cr = new OrganizerGetScheduleRequest(25);
     	String organizerRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(organizerRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("getorganized"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	OrganizerGetScheduleResponse resp = new Gson().fromJson(post.body, OrganizerGetScheduleResponse.class);
     	Assert.assertEquals(resp.schedules, resp.schedules);
	}
}
