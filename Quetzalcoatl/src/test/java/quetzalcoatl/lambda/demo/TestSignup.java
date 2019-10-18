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


public class TestSignup {

	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		SignUpHandler handler = new SignUpHandler();
     	SignupRequest cr = new SignupRequest("Pratticus","prattata");
     	String createRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(createRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("signup"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	SignupResponse resp = new Gson().fromJson(post.body, SignupResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
