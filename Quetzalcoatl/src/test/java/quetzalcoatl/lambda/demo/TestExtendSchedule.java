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

public class TestExtendSchedule {
	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testExtend() throws IOException {
		ExtendScheduleHandler handler = new ExtendScheduleHandler();
	 	Date end = new Date();
     	ExtendScheduleRequest er = new ExtendScheduleRequest(174, end);
     	String extendRequest = new Gson().toJson(er);
     	String jsonRequest = new Gson().toJson(new PostRequest(extendRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("extenddates"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	ExtendScheduleResponse resp = new Gson().fromJson(post.body, ExtendScheduleResponse.class);
     	Assert.assertEquals(202, resp.httpCode);
	}
}
