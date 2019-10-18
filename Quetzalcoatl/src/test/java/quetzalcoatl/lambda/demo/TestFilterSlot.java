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

public class TestFilterSlot {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		FilterSlotHandler handler = new FilterSlotHandler();
	 	Date start = new Date();
	 	Date end = new Date();
     	FilterSlotRequest cr = new FilterSlotRequest(174, 2018, -1, -1, 4, 15);
     	String filterRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(filterRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("filterslots"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	FilterSlotResponse resp = new Gson().fromJson(post.body, FilterSlotResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
