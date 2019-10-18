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

public class TestLogin {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		LoginHandler handler = new LoginHandler();
	 	Date start = new Date();
	 	Date end = new Date();
     	LoginRequest lr = new LoginRequest("Andrew", "Nolam");
     	String loginRequest = new Gson().toJson(lr);
     	String jsonRequest = new Gson().toJson(new PostRequest(loginRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("login"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	LoginResponse resp = new Gson().fromJson(post.body, LoginResponse.class);
     	Assert.assertEquals("Andrew", resp.username);
	}
}
