package quetzalcoatl.db;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import quetzalcoatl.model.ExtendedSchedule;
import quetzalcoatl.model.Schedule;

public class ScheduleDAO {

	java.sql.Connection conn;

    public ScheduleDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }

    public int deleteSchedule(ArrayList<Integer> deleteIDs) throws Exception {
    	try {
    		int resultSet2=0;
    		for(int i = 0; i<deleteIDs.size();i++) {
    			int deleteID = deleteIDs.get(i);
    			PreparedStatement ps = conn.prepareStatement("DELETE FROM schedules WHERE scheduleid2=?;");
    			ps.setInt(1, deleteID);
                int resultSet = ps.executeUpdate();
    			PreparedStatement ps2 = conn.prepareStatement("DELETE FROM slots WHERE scheduleid=?;");
    			ps2.setInt(1, deleteID);
                ps2.executeUpdate();
                PreparedStatement ps3 = conn.prepareStatement("DELETE FROM participation WHERE scheduleId=?;");
                ps3.setInt(1, deleteID);
                ps3.executeUpdate();
    			ps.close();
    			ps2.close();
    			ps3.close();
    			resultSet2++;
    		}
    		return resultSet2;
    	} catch(Exception e) {
        	e.printStackTrace();
        	throw new Exception("Failed to delete schedule: " + e.getMessage());
    	}
    }
    public Schedule getSchedule(int id2) throws Exception {
        
        try {
            Schedule schedule = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedules WHERE scheduleid2=?;");
            ps.setInt(1, id2);
            ResultSet resultSet = ps.executeQuery();
            
            while (resultSet.next()) {
                schedule = generateSchedule(resultSet);
            }
            resultSet.close();
            ps.close();
            
            return schedule;

        } catch (Exception e) {
        	e.printStackTrace();
            throw new Exception("Failed in getting schedule: " + e.getMessage());
        }
    }
    
    public int getSlotLength(int id2) throws Exception {
    	try {
    		int responseint = 0;
    		PreparedStatement ps = conn.prepareStatement("SELECT slotlength FROM schedules WHERE scheduleid2=?;");
    		ps.setInt(1, id2);
    		ResultSet resultSet = ps.executeQuery();
    		
    		while (resultSet.next()) {
    			responseint = resultSet.getInt("slotlength");
    		}
    		resultSet.close();
    		ps.close();
    		return responseint;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed in getting schedule: " + e.getMessage());
    	}
    }
    public ArrayList<ExtendedSchedule> organizerGetSchedules(int orgID) throws Exception {
    	
    	try {
    		ArrayList<ExtendedSchedule> orgSchedules = new ArrayList<ExtendedSchedule>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedules s join users u on u.Id = s.organizerId2 WHERE s.organizerId2=?;");
            ps.setInt(1, orgID);
            ResultSet resultSet = ps.executeQuery();
            
            while(resultSet.next()) {
            	orgSchedules.add(generateExtendedSchedule(resultSet));
            }
            resultSet.close();
    		return orgSchedules;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed to get schedules: " + e.getMessage());
    	}
    }
    public ArrayList<ExtendedSchedule> getAdminSchedule(String search) throws Exception{
    	try {
    		ArrayList<ExtendedSchedule> schedules = new ArrayList<ExtendedSchedule>();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    		PreparedStatement ps = conn.prepareStatement("select * from schedules s left join users u on u.Id = s.organizerId2 where creationDate > ?;");
    		ps.setTimestamp(1, new java.sql.Timestamp(sdf.parse(search).getTime()));
    		ResultSet resultSet = ps.executeQuery();
    		while(resultSet.next()) {
    			schedules.add(generateExtendedSchedule(resultSet));
    		}
    		resultSet.close();
    		return schedules;
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new Exception("Failed in getting schedules: " + e.getMessage());
    	}
    }
    
    public int deleteAdminSchedule(String search) throws Exception{
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");
    		PreparedStatement ps = conn.prepareStatement("SELECT * from schedules where creationDate < ?;");
    		ps.setTimestamp(1, new java.sql.Timestamp(sdf.parse(search).getTime()));
    		ResultSet resultSet = ps.executeQuery();
    		ArrayList<Integer> toDelete = new ArrayList<Integer>();
    		while(resultSet.next()) {
    			toDelete.add(resultSet.getInt("scheduleid2"));
    		}
    		resultSet.close();
    		return deleteSchedule(toDelete);
    	}catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed to delete: "+e.getMessage());
    	}
    }
    
    public boolean deleteSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM schedules WHERE scheduleid = ?;");
            ps.setString(1, schedule.Id);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);

        } catch (Exception e) {
            throw new Exception("Failed to delete schedule: " + e.getMessage());
        }
    }
  
    //update will very much not work right now hopefully it isn't needed yet
    //it needs to be fixed at some point ok, please
    public boolean updateSchedule(Schedule schedule) throws Exception {
        try {
        	String query = "UPDATE schedules SET value=? WHERE name=?;";
        	PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, schedule.Id);
            ps.setString(2, schedule.Id);
            int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
        } catch (Exception e) {
            throw new Exception("Failed to update report: " + e.getMessage());
        }
    }
    
    public boolean extendSchedule(int schedID, Date extDate) throws Exception {
    	try {
    		Schedule sch = getSchedule(schedID);
    		if(sch.startDate.after(extDate)) {
    			new SlotDAO().extendSlots(sch, extDate, sch.slotLength, true);
	    		String query = "UPDATE schedules SET startdate=? WHERE scheduleid2=?;";
	    		PreparedStatement ps = conn.prepareStatement(query);
	    		ps.setInt(2, schedID);
	    		ps.setDate(1,  new java.sql.Date(extDate.getTime()));
	    		int isAffected = ps.executeUpdate();
	    		ps.close();	    		
	    		return (isAffected == 1);

    		} else if(sch.endDate.before(extDate)){
    			new SlotDAO().extendSlots(sch, extDate, sch.slotLength, false);
    			String query = "UPDATE schedules SET enddate=? WHERE scheduleid2=?;";
	    		PreparedStatement ps = conn.prepareStatement(query);
	    		ps.setInt(2, schedID);
	    		ps.setDate(1,  new java.sql.Date(extDate.getTime()));
	    		int isAffected = ps.executeUpdate();
	    		ps.close();	    		
	    		return (isAffected == 1);
    		}else {
    			return false;
    		}
    	} catch (Exception e) {
    		throw new Exception("Failed to extend schedule: " + e.getMessage());
    	}
    }
    
    public int addSchedule(Schedule schedule) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM schedules WHERE scheduleid = ?;");
            ps.setString(1, schedule.Id);
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                Schedule c = generateSchedule(resultSet);
                resultSet.close();
                return -1;
            }

            ps = conn.prepareStatement("INSERT INTO schedules (startdate,enddate,scheduleid,starttime,endtime,slotlength,organizerId,creationDate, organizerId2, participationCode, name) values(?,?,?,?,?,?,?,?,?,?,?);");
            ps.setDate(1,  new java.sql.Date(schedule.startDate.getTime()));            
            ps.setDate(2,  new java.sql.Date(schedule.endDate.getTime()));
            ps.setString(3, schedule.Id);
            ps.setInt(4, schedule.startTime);
            ps.setInt(5, schedule.endTime);
            ps.setInt(6, schedule.slotLength);
            ps.setInt(7, schedule.organizerId);
            ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
            ps.setInt(9, schedule.id2);
            String ugh = "" + Math.random();
            ps.setString(10, ugh);
            ps.setString(11, schedule.name);
            ps.execute();
            
            // Execute a query to determine the ID of the previously inserted schedule
            PreparedStatement ps2 = conn.prepareStatement("SELECT @@IDENTITY as butt");
            ps2.executeQuery();
            ResultSet rs = ps2.getResultSet();
            if(rs.next()) {
            	return rs.getInt("butt");
            } else {
            	return -2;
            }
            //return true;

        } catch (Exception e) {
        	StringWriter sw = new StringWriter();
        	PrintWriter pw = new PrintWriter(sw);
        	e.printStackTrace(pw);
        	String sStackTrace = sw.toString(); // stack trace as a string
            throw new Exception("Failed to insert schedule: " + e.getMessage() + "[" + sStackTrace + "]");
        }
    }

    public List<Schedule> getAllschedules() throws Exception {
        
        List<Schedule> allSchedules = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM schedules";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Schedule c = generateSchedule(resultSet);
                allSchedules.add(c);
            }
            resultSet.close();
            statement.close();
            return allSchedules;

        } catch (Exception e) {
            throw new Exception("Failed in getting books: " + e.getMessage());
        }
    }
    
    public Schedule getParticularSchedules(String code) throws Exception {
    	List<Schedule> partSchedules = new ArrayList<>();
    	try {
    		List<Schedule> allSchedules = this.getAllschedules();
    		for(int i = 0; i<allSchedules.size();i++) {
    			if(allSchedules.get(i).participationCode.equals(code))
    				partSchedules.add(allSchedules.get(i));
    		}
    		if(partSchedules.isEmpty())
    			throw new Exception("ARRRRGGGGGHHH NOT A SCHEDDDDULLLEEE DUMMMYYY!!!!");
    		else
    			return partSchedules.get(0);
    		
    	}catch(Exception e) {
    		throw new Exception("Schedule with participationCode: " + code + " does not exist :(");
    	}
    	
    }
    
    private Schedule generateSchedule(ResultSet resultSet) throws Exception {
    	int organizerId = resultSet.getInt("organizerid2");
    	int startTime = resultSet.getInt("starttime");
    	int endTime = resultSet.getInt("endtime");
    	int slotLength = resultSet.getInt("slotlength");
    	Date startDate = resultSet.getDate("startdate");
    	Date endDate = resultSet.getDate("enddate");
    	String Id = resultSet.getString("scheduleid");
    	int realId = resultSet.getInt("scheduleid2");
    	int Id2 = resultSet.getInt("organizerId2");
    	String participationCode = resultSet.getString("participationCode");   	
    	String name = resultSet.getString("name");
        return new Schedule(organizerId, startTime, endTime, slotLength, startDate, endDate,Id2, realId, participationCode, name);
    }
    
    private ExtendedSchedule generateExtendedSchedule(ResultSet resultSet) throws Exception {
    	int organizerId = resultSet.getInt("organizerid2");
    	int startTime = resultSet.getInt("starttime");
    	int endTime = resultSet.getInt("endtime");
    	int slotLength = resultSet.getInt("slotlength");
    	Date startDate = resultSet.getDate("startdate");
    	Date endDate = resultSet.getDate("enddate");
    	String Id = resultSet.getString("scheduleid");
    	int realId = resultSet.getInt("scheduleid2");
    	int Id2 = resultSet.getInt("organizerId2");
    	String participationCode = resultSet.getString("participationCode"); 
    	String username = resultSet.getString("username");
    	String name = resultSet.getString("name");
        return new ExtendedSchedule(organizerId, startTime, endTime, slotLength, startDate, endDate,Id2, realId, participationCode,username, name);
    }

}