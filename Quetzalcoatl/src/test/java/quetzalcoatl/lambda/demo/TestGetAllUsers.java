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

public class TestGetAllUsers {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testGetAllUsers() throws IOException {
		GetAllUsersHandler handler = new GetAllUsersHandler();
	 	Date start = new Date();
	 	Date end = new Date();
     	GetAllUsersRequest gr = new GetAllUsersRequest();
     	String getAllUsersRequest = new Gson().toJson(gr);
     	String jsonRequest = new Gson().toJson(new PostRequest(getAllUsersRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("getallusers"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	GetAllUsersResponse resp = new Gson().fromJson(post.body, GetAllUsersResponse.class);
     	Assert.assertEquals(resp.names, resp.names);
	}
}
