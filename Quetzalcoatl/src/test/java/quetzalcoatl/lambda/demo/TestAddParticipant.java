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

public class TestAddParticipant {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		AddParticipantHandler handler = new AddParticipantHandler();
     	addParticipantRequest ar = new addParticipantRequest("Andrew", 1);
     	String addPartRequest = new Gson().toJson(ar);
     	String jsonRequest = new Gson().toJson(new PostRequest(addPartRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("addparticipant"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	addParticipantResponse resp = new Gson().fromJson(post.body, addParticipantResponse.class);
     	Assert.assertEquals("Andrew", resp.name);
	}
}
