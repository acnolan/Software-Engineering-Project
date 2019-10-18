package quetzalcoatl.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import quetzalcoatl.model.ExtendedSchedule;
import quetzalcoatl.model.Schedule;

public class ParticipationDAO {
	java.sql.Connection conn;

    public ParticipationDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public ArrayList<ExtendedSchedule> getParticipatingSchedules(int participantId) throws Exception {
    	try {
    		ArrayList<ExtendedSchedule> schedules = new ArrayList<ExtendedSchedule>();
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM participation p join schedules s on s.scheduleId2 = p.scheduleId AND p.participantId=? join users u on u.id=s.organizerId2;");
    		ps.setInt(1, participantId);
    		ResultSet resultSet = ps.executeQuery();
    		while(resultSet.next()) {
    			schedules.add(generateSchedule(resultSet));
    		}
    		resultSet.close();
    		return schedules;
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new Exception("Failed in getting schedules: " + e.getMessage());
    	}
    }
    
    public void addParticipant(String name, int schId) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username =?;");
    		ps.setString(1, name);
    		ResultSet resultSet = ps.executeQuery();
    		resultSet.next();
    		int id = resultSet.getInt("id");
    		ps = conn.prepareStatement("INSERT INTO participation (participantId,scheduleId) values(?, ?);");
    		ps.setInt(1, id);
    		ps.setInt(2, schId);
    		ps.execute();
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("that's not a name fucko");
    	}
    }
    
    public void joinSchedule(int id, String code)throws Exception{
    	try {
    		ScheduleDAO sch = new ScheduleDAO();
    		sch.getParticularSchedules(code);
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedules where participationCode=?;");
    		ps.setString(1, code);
    		ResultSet resultSet = ps.executeQuery();
    		resultSet.next();
    		int id2 = resultSet.getInt("scheduleid2");
    		ps = conn.prepareStatement("INSERT INTO participation (participantId, scheduleId) values(?,?);");
    		ps.setInt(1, id);
    		ps.setInt(2, id2);
    		ps.execute();
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("YOU DONE GOOFED");
    	}
    }
    
    private ExtendedSchedule generateSchedule(ResultSet resultSet) throws Exception {
    	String organizerId = resultSet.getString("organizerid");
    	int startTime = resultSet.getInt("starttime");
    	int endTime = resultSet.getInt("endtime");
    	int slotLength = resultSet.getInt("slotlength");
    	Date startDate = resultSet.getDate("startdate");
    	Date endDate = resultSet.getDate("enddate");
    	String Id = resultSet.getString("scheduleid");
    	int realId = resultSet.getInt("scheduleid2");
    	int Id2 = resultSet.getInt("organizerId2");
    	String participationCode = resultSet.getString("participationCode");
    	String username= resultSet.getString("username");
    	String name = resultSet.getString("name");
        return new ExtendedSchedule(Id2, startTime, endTime, slotLength, startDate, endDate,Id2, realId, participationCode,username,name);
    }
}
