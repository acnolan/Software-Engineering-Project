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

public class TestGetSlots {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		GetSlotsHandler handler = new GetSlotsHandler();
     	GetSlotsRequest cr = new GetSlotsRequest(174);
     	String getSlotsRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(getSlotsRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("getslots"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	GetSlotsResponse resp = new Gson().fromJson(post.body, GetSlotsResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
