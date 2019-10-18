package quetzalcoatl.db;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import quetzalcoatl.model.Schedule;

public class UserDAO {

	java.sql.Connection conn;

    public UserDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    public int[] login(String username, String password) throws Exception {
    	try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?;");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            int id=resultSet.getInt("id");
            int isAdmin = resultSet.getInt("isSysAdmin");
            resultSet.close();
            ps.close();
            
            int[] results = new int[2];
            results[0]=id;
            results[1]=isAdmin;
            return results;

        } catch (Exception e) {
        	e.printStackTrace();
          throw new Exception("Failed to login: " + e.getMessage());
        }
    }
    
    public String[] createUser(String username, String password) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO users (username,password) values(?,?);");
            ps.setString(1,  username);            
            ps.setString(2,  password);
            ps.execute();            
            String[] result = new String[2];
            result[0]=username;
            result[1]=password;
            return result;
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("yeet");
    	}
	}
    
    public ArrayList<String> getAllUsers() throws Exception{
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM users;");
    		ResultSet resultSet = ps.executeQuery();
            ArrayList<String> results = new ArrayList<String>();
    		while(resultSet.next()) {
            	results.add(resultSet.getString("username"));
            }
            return results;
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("yeet");
    	}
    }
}