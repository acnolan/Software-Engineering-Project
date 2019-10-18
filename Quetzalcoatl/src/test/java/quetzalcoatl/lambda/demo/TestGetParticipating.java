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

public class TestGetParticipating {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testParticipating() throws IOException {
		ParticipatingScheduleHandler handler = new ParticipatingScheduleHandler();
     	ParticipatingScheduleRequest pr = new ParticipatingScheduleRequest(25);
     	String createRequest = new Gson().toJson(pr);
     	String jsonRequest = new Gson().toJson(new PostRequest(createRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("getparticipating"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	ParticipatingScheduleResponse resp = new Gson().fromJson(post.body, ParticipatingScheduleResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
