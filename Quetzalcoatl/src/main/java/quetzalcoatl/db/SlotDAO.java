package quetzalcoatl.db;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import quetzalcoatl.lambda.demo.FilterSlotRequest;
import quetzalcoatl.model.ExtendedSlot;
import quetzalcoatl.model.Schedule;
import quetzalcoatl.model.Slot;

public class SlotDAO {

	java.sql.Connection conn;

    public SlotDAO() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
    
    public Slot getSlot(int id) throws Exception {
    	try {
    		
    		Slot slot = null;
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE id=?");
    		ps.setInt(1, id);
    		ResultSet rs = ps.executeQuery();
    		if(!rs.next()) {
    			// Not found
    			rs.close();
    			ps.close();
    			return null;
    		}
    		// Create the slot
    		slot = new Slot(
    			rs.getInt("id"),
    			rs.getInt("scheduleid"),
    			rs.getDate("datetime"),
    			rs.getInt("bookedby"));
    		// Return the slot
    		return slot;
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed in getting slot: " + e.getMessage());
    	}
    }
    
    public boolean deleteSlot(Slot slot) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("DELETE FROM slots WHERE id=?;");
    		ps.setInt(1, slot.id);
    		int numAffected = ps.executeUpdate();
    		ps.close();
    		return numAffected == 1;
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new Exception("Failed to delete slot: " + e.getMessage());
    	}
    }
    
    public boolean bookSlot(int slotID, int participantID) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("UPDATE slots SET bookedby=? WHERE id=?;");
    		ps.setInt(1, participantID);
    		ps.setInt(2, slotID);
    		int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
    	} catch (Exception e) {
            throw new Exception("Failed to book slot: " + e.getMessage());
        }
    }
    
    public void switchAllSlots(boolean isClose, List<Integer> los) throws Exception {
    	try {
    		for(Integer s: los) {
    			switchSlot(isClose, s);
    		}
    	} catch (Exception e) {
    		throw new Exception("Failed to toggle slot: " + e.getMessage());
    	}
    }
    
    public boolean switchSlot(boolean isClose, Integer slotid) throws Exception {
  		int booked = 0;
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT bookedby FROM slots WHERE id=?;"); // SELECT * FROM slots WHERE id=?
    		ps.setInt(1, slotid);
      		ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                booked = resultSet.getInt("bookedby");
            }
            resultSet.close();
            ps.close();
    		booked = (isClose) ? -1 : 0;
    		PreparedStatement ps2 = conn.prepareStatement("UPDATE slots SET bookedby=? WHERE id=?;");
    		ps2.setInt(1, booked);
    		ps2.setInt(2, slotid);
    		int num = ps2.executeUpdate();
    		ps2.close();
    		
    		return (num == 1);
    	} catch (Exception e){
    		throw new Exception("Failed to toggle slot: " + e.getMessage());
    	}
    }
    
    public boolean insertSlot(Slot slot) throws Exception {
    	try {
    		PreparedStatement ps = conn.prepareStatement("INSERT INTO slots (scheduleid,datetime) values(?,?);");
    		ps.setInt(1, slot.scheduleid);
    		ps.setTimestamp(2, new java.sql.Timestamp(slot.datetime.getTime()));
    		int numAffected = ps.executeUpdate();
            ps.close();
            
            return (numAffected == 1);
    	} catch (Exception e) {
            throw new Exception("Failed to insert slot: " + e.getMessage());
        }
    }
    
    public List<ExtendedSlot> getAllSlots(String startTime, String endTime, int id2) throws Exception {
    	
    	List<ExtendedSlot> returnSlots = new ArrayList<ExtendedSlot>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots s LEFT JOIN users u on u.id = s.bookedby WHERE datetime > ? AND datetime < ? AND scheduleid=?;");
    		ps.setTimestamp(1, new java.sql.Timestamp(sdf.parse(startTime).getTime()));
    		ps.setTimestamp(2, new java.sql.Timestamp(sdf.parse(endTime).getTime()));
    		ps.setInt(3, id2);
    		ResultSet resultSet = ps.executeQuery();
    		
    		while (resultSet.next()) {
    			ExtendedSlot s = generateExtendedSlot(resultSet);
    			returnSlots.add(s);
    		}
    		resultSet.close();
    		ps.close();
    		return returnSlots;
    		
    	} catch (Exception e) {
    		throw new Exception("Failed in getting slots: " + e.getMessage());
    	}
    }
    
    public List<Slot> getScheduleSlots(int id2) throws Exception{
    	List<Slot> returnSlots = new ArrayList<Slot>();
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE scheduleid = ?;");
    		ps.setInt(1, id2);
    		ResultSet resultSet = ps.executeQuery();
    	
    		while (resultSet.next()) {
    			Slot s = generateSlot(resultSet);
    			returnSlots.add(s);
    		}
    		resultSet.close();
    		ps.close();
    		return returnSlots;
    	} catch (Exception e) {
    		throw new Exception("Failed in getting slots: " + e.getMessage());
    	}
    	
    }
    
    private Slot generateSlot(ResultSet resultSet) throws Exception {
    	int id = resultSet.getInt("id");
    	int scheduleid = resultSet.getInt("scheduleid");
    	java.util.Date datetime = resultSet.getTimestamp("datetime");
    	int bookedby = resultSet.getInt("bookedby");
    	return new Slot(id, scheduleid, datetime, bookedby);
    }
    
    public void extendSlots(Schedule sch, Date extDate, int slotLength, boolean isBefore) throws Exception {
    	int slotCount = (sch.endTime - sch.startTime) / slotLength;
    	if(isBefore) {
    		for(Date i = extDate; i.before(sch.startDate) || i.equals(sch.startDate); i = new Date(i.getTime() + TimeUnit.DAYS.toMillis(1))) {
    			// Iterate over slots
    			for(int s = 0;s < slotCount; s++) {
    				// Create the slot
    				Slot slot = new Slot(0,sch.realId,new Date(i.getTime() + TimeUnit.MINUTES.toMillis(sch.startTime + (s * slotLength))), 0);
    				// Insert the slot
    				this.insertSlot(slot);
    			}
    		}
    	}else {
    		for(Date i = sch.endDate; i.before(extDate) || i.equals(extDate); i = new Date(i.getTime() + TimeUnit.DAYS.toMillis(1))) {
    			// Iterate over slots
    			for(int s = 0;s < slotCount; s++) {
    				// Create the slot
    				Slot slot = new Slot(0,sch.realId,new Date(i.getTime() + TimeUnit.MINUTES.toMillis(sch.startTime + (s * slotLength))), 0);
    				// Insert the slot
    				this.insertSlot(slot);
    			}
    		}
    	}
    }
    
    private ExtendedSlot generateExtendedSlot(ResultSet resultSet) throws Exception {
    	int id = resultSet.getInt("id");
    	int scheduleid = resultSet.getInt("scheduleid");
    	java.util.Date datetime = resultSet.getTimestamp("datetime");
    	int bookedby = resultSet.getInt("bookedby");
    	String username = resultSet.getString("username");
    	return new ExtendedSlot(id, scheduleid, datetime, bookedby, username);
    }

    @SuppressWarnings("deprecation")
	public List<Slot> filterSlot(FilterSlotRequest req) throws Exception {
    	String responseString = "";
//    	booked = (isClose) ? 1 : 0;
//    	'%%%%-%%-%% %%:%%:%%'
//    	filtered by Year, Month, Day-Of-Week, Day-Of-Month, or Timeslot

    	List<Slot> returnSlots = new ArrayList<Slot>();
    	
    	responseString += (req.year > -1) ? (Integer.toString(req.year) + "-") : "%%%%-";
    	responseString += (req.month > -1) ? (Integer.toString(req.month / 10) + Integer.toString(req.month % 10) + "-") : "%%-";
    	responseString += (req.dayOfMonth > -1) ? (Integer.toString(req.dayOfMonth / 10) + Integer.toString(req.dayOfMonth % 10) + " ") : "%% ";
    	String hours = (req.timeslot / 60 < 10) ? ("0" + Integer.toString(req.timeslot / 60)) : Integer.toString(req.timeslot / 60);
    	String minutes = (req.timeslot % 60 < 10) ? ("0" + Integer.toString(req.timeslot % 60)) : Integer.toString(req.timeslot % 60);
    	responseString += (req.timeslot > -1) ? (hours + ":" + minutes + ":00") : "%%:%%:00";
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE datetime LIKE ? AND scheduleid = ? AND (bookedby IS NULL OR bookedby = 0);");
    		ps.setString(1, responseString);
    		ps.setInt(2, req.id2);
    		ResultSet resultSet = ps.executeQuery();
    		
    		while (resultSet.next()) {
    			Slot s = generateSlot(resultSet);
    			if((req.dayOfWeek == -1 || s.datetime.getDay() == req.dayOfWeek) && (s.datetime.getDay() != 0 && s.datetime.getDay() != 6)) {
        			returnSlots.add(s);
    			}
    		}
    		
    		resultSet.close();
    		ps.close();
    		return returnSlots;
    	} catch (Exception e) {
    		throw new Exception("Failed in getting slots: " + e.getMessage());
    	}
    }
    
}