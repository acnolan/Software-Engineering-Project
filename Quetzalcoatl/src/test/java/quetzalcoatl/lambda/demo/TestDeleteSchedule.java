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

public class TestDeleteSchedule {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		DeleteScheduleHandler handler = new DeleteScheduleHandler();
     	DeleteScheduleRequest dr = new DeleteScheduleRequest(1);
     	String deleteRequest = new Gson().toJson(dr);
     	String jsonRequest = new Gson().toJson(new PostRequest(deleteRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("deleteschedule"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	CreateScheduleResponse resp = new Gson().fromJson(post.body, CreateScheduleResponse.class);
     	Assert.assertEquals(202, resp.httpCode);
	}
}
