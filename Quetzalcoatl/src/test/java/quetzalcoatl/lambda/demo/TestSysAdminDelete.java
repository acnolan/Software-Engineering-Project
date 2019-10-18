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


public class TestSysAdminDelete {

	Context createContext(String apiCall) {
        TestContext ctx = new TestContext();
        ctx.setFunctionName(apiCall);
        return ctx;
    }
	
	@Test
	public void testCreate() throws IOException {
		SysAdminDeleteHandler handler = new SysAdminDeleteHandler();
     	SysAdminDeleteRequest cr = new SysAdminDeleteRequest(3);
     	String createRequest = new Gson().toJson(cr);
     	String jsonRequest = new Gson().toJson(new PostRequest(createRequest));
     
     	InputStream input = new ByteArrayInputStream(jsonRequest.getBytes());
     	OutputStream output = new ByteArrayOutputStream();

     	handler.handleRequest(input, output, createContext("sysadmindelete"));
     	PostResponse post = new Gson().fromJson(output.toString(), PostResponse.class);
     	SysAdminDeleteResponse resp = new Gson().fromJson(post.body, SysAdminDeleteResponse.class);
     	Assert.assertEquals(200, resp.httpCode);
	}
}
