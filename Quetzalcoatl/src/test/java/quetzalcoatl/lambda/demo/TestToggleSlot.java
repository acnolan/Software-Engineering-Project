package quetzalcoatl.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import quetzalcoatl.model.PostRequest;
import quetzalcoatl.model.PostResponse;


public class TestToggleSlot {

	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		ToggleSlotHandler handler = new ToggleSlotHandler();
	 	ArrayList<Integer> nums = new ArrayList<Integer>();
	 	nums.add(1);
	 	nums.add(2);
	 	nums.add(3);
     	ToggleSlotRequest cr = new ToggleSlotRequest(true,nums);
     	String toggleSlotRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(toggleSlotRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("toggleslots"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	ToggleSlotResponse resp = new Gson().fromJson(post.body, ToggleSlotResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
