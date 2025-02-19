
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import models.ControllerResponse;
import models.Event;
import models.EventItem;
import models.EventParticipation;
import models.User;
import models.Event;

import java.util.ArrayList;

/// DAO class, used to interface with the database and perform various data operations.
/// Author(s): Jamie Mizelle, Darien Dalton, Abdinasir Aidrus (Nas), Lisa Gehrt
/// Date: 9/29/24
@Repository
public class DAO
{
  // read in DB info from application.properties
  @Value("${spring.datasource.username}")
  private String DB_USER;

  @Value("${spring.datasource.password}")
  private String DB_PASSWORD;

  @Value("${spring.datasource.url}")
  private String DB_URL;

  @Value("${spring.datasource.driver-class-name}")
  private String DB_DRIVER_CLASS_NAME;

  // getUserByEmailAndPassword function, tries to pull a user from the database based on an email and password input
  public ControllerResponse getUserByEmailAndPassword(String userEmailInput, String userPasswordInput)
  {
    // init cr and user objects
    ControllerResponse cr = new ControllerResponse();
    User user = new User();

    // begin database related code
    try
    {
      // try to get db driver class
      try
      {
        Class.forName(DB_DRIVER_CLASS_NAME);
      }
      catch (ClassNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      // get connection to db
      Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

      // init SQL query, this will select all users with a certain email and password
      String query = "SELECT * FROM users WHERE email=? AND password=?";

      // prepare the statement and set the '?' parameters
      PreparedStatement st = con.prepareStatement(query);
      st.setString(1, userEmailInput);
      st.setString(2, userPasswordInput);

      // execute query
      ResultSet myRs = st.executeQuery();

      // go through the RS (should be only one entry) and map to user object
      while (myRs.next())
      {

        user = mapRowToUser(myRs);

      }

      // close statement and connection
      st.close();
      con.close();
    }
    catch (SQLException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {

    }

    // set user on ControllerResponse object
    cr.setUser(user);

    // check to see if there was an error getting the user
    if (user.getId() == -1)
    {
      cr.getErrors().add("Error, account not found");
    }

    // return ControllerResponse
    return cr;
  }

  
  //Matthew Miller 10/3/24
  // tryToRegisterUser function, used to register a user for the application
  // this will check if the user email already exists, if so it will return an error
  // otherwise it will insert a new User.
  public ControllerResponse tryToRegisterUser(User input) 
  {
	  
	  ControllerResponse cr = new ControllerResponse();
	  User user = new User();
	  
	    // begin database related code
	    try 
	    {
	        // try to get db driver class
	        try 
	        {
	            Class.forName(DB_DRIVER_CLASS_NAME);
	        } 
	        catch (ClassNotFoundException e) 
	        {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        // get connection to db
	        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // init SQL query, this will select all users with a certain email and password
	        String query = "SELECT * FROM users WHERE email=?";
	        
	        // prepare the statement and set the '?' parameters
	        PreparedStatement st = con.prepareStatement(query);
	        st.setString(1, input.getEmail());

	        // execute query
	        ResultSet myRs = st.executeQuery();
	        
	        // go through the RS (should be only one entry) and map to user object
	        while (myRs.next()) 
	        {

	          user = mapRowToUser(myRs);
	          
	        }
	        
	        //If the current user doesn't exist, add user to database
	        if (user.getId() == -1) 
	        {
	        	String insert = "INSERT INTO users (first_name, last_name, email, password)" + "values (?, ?, ?, ?)";
	        	PreparedStatement insertStatement = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
	        	
	        	
	        	insertStatement.setString(1, input.getFirstName());
	        	insertStatement.setString(2,  input.getLastName());
	        	insertStatement.setString(3,  input.getEmail());
	        	insertStatement.setString(4,  input.getPassword());
	        	
	        	int affectedRows = insertStatement.executeUpdate();
	        	
	        	if (affectedRows != 0) 
	        	{
	        	  try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) 
	        	  {
	              if (generatedKeys.next()) 
	              {
	                  user.setId(generatedKeys.getInt(1));
	              }
	              
	        	  }
	     
	        	}
	        	
	        	insertStatement.close();
	        }
	        else
	        {
	          // in this case, there is a user with the same email
	          // reset id to -1 so frontend knows registration fails
	          user.setId(-1);
	        }
	      }
	    	catch (SQLException e) 
	      {
	    	  // TODO Auto-generated catch block
	    	  e.printStackTrace();
	      }
	    	finally
	    	{
      
	    	}
	  
	  
	  //If it exists, return user with id = -1, else add user to the db with a new id;
	  cr.setUser(user);
	  return cr;
  }
  

  // LG
  // getAllEvents method returns a list of all events 
  public ArrayList<Event> getAllEvents() {
	  ArrayList<Event> events = new ArrayList<>();
	  
	  // begin database related code
	  try 
	  {
	      // try to get db driver class
	      try 
	      {
	          Class.forName(DB_DRIVER_CLASS_NAME);
	      } 
	      catch (ClassNotFoundException e) 
	      {
	          e.printStackTrace();
	      }
	
	      // get connection to db
	      Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	
	      // init SQL query, this will select all events
	      String query = "SELECT * FROM events";
		  Statement stmt = con.createStatement();
		  ResultSet rs = stmt.executeQuery(query);
		  // adds the event mapping to list of events
		  while (rs.next()) {
			  events.add(mapRowToEvent(rs));
		  }
	  } catch (SQLException e) {
		  e.printStackTrace();
	  }
	  
	  return events;
  }
  
  
  // method to insert event
  public ControllerResponse insertEvent(Event event, int userId)
  {
    ControllerResponse cr = new ControllerResponse();
    
    // begin database related code
    try
    {
      // try to get db driver class
      try
      {
        Class.forName(DB_DRIVER_CLASS_NAME);
      }
      catch (ClassNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      // get connection to db
      Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

      // insert statement
      String statement = "INSERT INTO events(owner_user_id, name, description, date_time) VALUES (?, ?, ?, ?)";

      // prepare the statement and set the '?' parameters
      PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
      

      st.setInt(1, userId);
      st.setString(2, event.getName());
      st.setString(3, event.getDescription());
      st.setTimestamp(4, Timestamp.valueOf(event.getDateTime()));
      
      int affectedRows = st.executeUpdate();
      
      try (ResultSet generatedKeys = st.getGeneratedKeys()) 
      {
        if (generatedKeys.next()) 
        {
            cr.setLastInsertedId(generatedKeys.getInt(1));
        }
        else 
        {
            System.out.println("FAILURE");
            cr.getErrors().add("Insert failed, Event Creation unsuccessful");
        }
      }
      // close statement and connection
      st.close();
      con.close();
    }
    catch (SQLException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {

    }

    
    return cr;
  }
  
  
  // insert eventParticipation 
  public ControllerResponse insertEventParticipation(int eventId, int userId)
  {
    ControllerResponse cr = new ControllerResponse();
    
      // begin database related code
       try
       {
         // try to get db driver class
         try
         {
           Class.forName(DB_DRIVER_CLASS_NAME);
         }
         catch (ClassNotFoundException e)
         {
           // TODO Auto-generated catch block
           e.printStackTrace();
         }

         // get connection to db
         Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

         // insert statement
         String statement = "INSERT INTO event_participation(event_id, user_id) VALUES (?, ?)";

         // prepare the statement and set the '?' parameters
         PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
         
         st.setInt(1, eventId);
         st.setInt(2, userId);
         
         // excute update
         int affectedRows = st.executeUpdate();
         
         try (ResultSet generatedKeys = st.getGeneratedKeys()) 
         {
           if (generatedKeys.next()) 
           {
               cr.setLastInsertedId(generatedKeys.getInt(1));
           }
           else 
           {
               System.out.println("FAILURE");
               cr.getErrors().add("Event Participation Failed");
           }
         }
         // close statement and connection
         st.close();
         con.close();
       }
       catch (SQLException e)
       {
         // TODO Auto-generated catch block
         e.printStackTrace();
       }
       finally
       {

       }

       
       return cr;
  }
  
  
  // method to insert a list of event items
  public ControllerResponse insertEventItems(int eventId, List<EventItem> items)
  {
    ControllerResponse cr = new ControllerResponse();
    
    // begin database related code
    try
    {
      // try to get db driver class
      try
      {
        Class.forName(DB_DRIVER_CLASS_NAME);
      }
      catch (ClassNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      // get connection to db
      Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
      con.setAutoCommit(false);

      String statement = "INSERT INTO event_item(event_id, user_id, name) VALUES (?, ?, ?);";
      
      // prepare the statement and set the '?' parameters
      PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
      
      for(EventItem item : items)
      {
        st.setInt(1, eventId);
        
        // item has an assigned user
        if(item.getUserId() != -1)
        {
          st.setInt(2, item.getUserId());
        }
        else
        {
          // item does not have an assigned user
          st.setNull(2, Types.INTEGER);
        }
     
        st.setString(3, item.getName());
        st.addBatch();
      }
      
      
      
      int[] affectedRows = st.executeBatch();
      
      //Explicitly commit statements to apply changes
      con.commit();
       
      // check to see if sum of affectedRows is equal to the number of items passed in
      int sumAffected = 0;
      for(int row : affectedRows)
      {
        sumAffected += row;
      }
      // add the error if applicable
      if(sumAffected != items.size())
      {
        cr.getErrors().add("Error occured when adding event items, could have partially saved some");
      }
      
      
      // close statement and connection
      st.close();
      con.close();
    }
    catch (SQLException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    finally
    {

    }

    
    return cr;
  }
  

  //MM 10/16/24
  //deleteEventItemsByEventId deletes list of event items for an event
  public ControllerResponse deleteEventItemsByEventId(int eventId) {

      ControllerResponse cr = new ControllerResponse();
      // begin database related code
      try {
          // try to get db driver class
          try {
              Class.forName(DB_DRIVER_CLASS_NAME);
          } catch (ClassNotFoundException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }

          // get connection to db
          Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

          // insert statement
          String statement = "DELETE FROM event_item WHERE event_id=?";

          // prepare the statement and set the '?' parameters
          PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

          st.setInt(1, eventId);

          int affectedRows = st.executeUpdate();

          if (affectedRows > 0) {} else {
              System.out.println("FAILURE");
              cr.getErrors().add("Delete Item Failed");
          }

          st.close();
          con.close();
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
      finally {

      }

      return cr;

  }
  
 
  
  
  // JM
  // getEventById
  //pretty self explanitory :)
  public Event getEventById(int eventId)
  {
    Event event = new Event();
    
    try 
    {
        // try to get db driver class
        try 
        {
            Class.forName(DB_DRIVER_CLASS_NAME);
        } 
        catch (ClassNotFoundException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        // get connection to db
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  
        // init SQL query, this will select an event by id
        String query = "SELECT * FROM events WHERE id=?";
        
        PreparedStatement stmt = con.prepareStatement(query);
        
        // set event id
        stmt.setInt(1, eventId);
        
        ResultSet rs = stmt.executeQuery();
        
        // creates event from RS
        while (rs.next()) 
        {
          event = this.mapRowToEvent(rs);
        }
    } 
    catch (SQLException e) 
    {
      e.printStackTrace();
    }
    
    
    
    return event;
  }
  
  // JM
  // getEventParticipationByEventIdAndUserId DAO method
  // pretty self explanitory :)
  public EventParticipation getEventParticipationByEventIdAndUserId(int eventId, int userId)
  {
    EventParticipation ep = new EventParticipation();
    
    
    try 
    {
        // try to get db driver class
        try 
        {
            Class.forName(DB_DRIVER_CLASS_NAME);
        } 
        catch (ClassNotFoundException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        // get connection to db
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  
        // init SQL query, this will select a EventParticipation
        String query = "SELECT * FROM event_participation WHERE event_id=? AND user_id=?";
        
        PreparedStatement stmt = con.prepareStatement(query);
        
        
        stmt.setInt(1, eventId);
        stmt.setInt(2, userId);
        
        ResultSet rs = stmt.executeQuery();
       
        while (rs.next()) 
        {
          // map EP using mapper function
          ep = this.mapRowToEventParticipation(rs);
        }
    } 
    catch (SQLException e) 
    {
      e.printStackTrace();
    }
    
    
    return ep;
  }
  
  
  
  // join event DAO method, takes an eventId and userId to generate a EventParticipation
  // object, itll check before it inserts this to ensure user is not already in event
  public ControllerResponse joinEvent(int eventId, int userId)
  {
    ControllerResponse cr = new ControllerResponse();
    
    // see if user is already in event
    EventParticipation ep = this.getEventParticipationByEventIdAndUserId(eventId, userId);
    
    if(ep.getId() != -1)
    {
      // this is bad, user is already in the event
      // realistically this shouldnt happen because conditional rendering on event_details JSP
      // but if user manipulates POST requests this could happen
      
      cr.getErrors().add("Error, you are already in the event");
    }
    else
    {
      // otherwise insert EP (join event)
      cr = this.insertEventParticipation(eventId, userId);
    }
    
    return cr;
  }
  
  
  
 //getEventItemsByEventId method returns a list of all event items for a particular Event
 public ArrayList<EventItem> getEventItemsByEventId(int eventId) 
 {
   ArrayList<EventItem> eventItems = new ArrayList<>();
   
   // begin database related code
   try 
   {
       // try to get db driver class
       try 
       {
           Class.forName(DB_DRIVER_CLASS_NAME);
       } 
       catch (ClassNotFoundException e) 
       {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
 
       // get connection to db
       Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
 
       // init SQL query, this will select all event items for an event
       String query = "SELECT event_item.id, event_item.event_id, event_item.user_id, event_item.name, users.first_name, users.last_name "
           + "FROM event_item "
           + "LEFT JOIN users "
           + "ON event_item.user_id = users.id WHERE event_id=?";
       
       
       PreparedStatement stmt = con.prepareStatement(query);
       
       stmt.setInt(1, eventId);
       
       ResultSet rs = stmt.executeQuery();
       
       // adds the event items to list of EventItems via mapper function
       while (rs.next()) 
       {
         eventItems.add(mapRowToEventItem(rs));
       }
     } 
     catch (SQLException e) 
     {
       e.printStackTrace();
     }
   
   return eventItems;
 }

 
  
 
  // JM
  // leaveEvent DAO method, deletes EventParticipation object that associates user with event
  // then updates all event items that user had claimed for the event and sets the user id to null
  public ControllerResponse leaveEvent(int eventId, int userId)
  {
    ControllerResponse cr = new ControllerResponse();
    
    
    
    // begin database related code
    
    Connection con = null;
    try
    {
      // try to get db driver class
      try
      {
        Class.forName(DB_DRIVER_CLASS_NAME);
        
      }
      catch (ClassNotFoundException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      
      
      // get connection to db
      con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
      
      //starts a transaction
      con.setAutoCommit(false);

      // updates statement
      String deleteStatement = "DELETE FROM event_participation WHERE event_id=? AND user_id=?";
      String deleteFreeformEventItemStatement = "DELETE FROM freeform_event_item WHERE event_id=? AND user_id=?";
      String updateStatement = "UPDATE event_item SET user_id=? WHERE event_id=? AND user_id=?";

      // prepare the statements and set the '?' parameters
      PreparedStatement deletePS = con.prepareStatement(deleteStatement, Statement.RETURN_GENERATED_KEYS);
      PreparedStatement deleteFreeformItemsPS = con.prepareStatement(deleteFreeformEventItemStatement, Statement.RETURN_GENERATED_KEYS);
      PreparedStatement updatePS = con.prepareStatement(updateStatement, Statement.RETURN_GENERATED_KEYS);
      
      // delete statement
      deletePS.setInt(1, eventId);
      deletePS.setInt(2, userId);
      
      // delete freeform item statement
      deleteFreeformItemsPS.setInt(1, eventId);
      deleteFreeformItemsPS.setInt(2, userId);
      
      
      // update statement
      updatePS.setNull(1, Types.INTEGER);
      updatePS.setInt(2, eventId);
      updatePS.setInt(3, userId);
      
      
      int affectedRows1 = deletePS.executeUpdate();
      int affectedRows2 = updatePS.executeUpdate();
      int affectedRows3 = deleteFreeformItemsPS.executeUpdate();
      
      if(affectedRows1 + affectedRows2 + affectedRows3 <= 0)
      {
        cr.getErrors().add("Error, Leave Event Failed");
        con.rollback();
      }
      else
      {
        // commit changes
        con.commit();
      }
      
      
      
      
      // close statement and connection
      deletePS.close();
      updatePS.close();
      con.close();
    }
    catch (SQLException e)
    {
      // catch errors, rollback unsaved changes
      e.printStackTrace();
      cr.getErrors().add("Error (Bad Connection), Leave Event Failed");
      if(con != null)
      {
        try
        {
          con.rollback();
        }
        catch (SQLException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
      
    }
    finally
    {

    }

    
    return cr;
  }
  
  
  
  // edit event item DAO method, this will update an event items user_id column
  // depending on the value of userId passed in, if the userId == -1, itll change it to null (unclaim item)
  // if the userId != -1, then itll save that userId as the new user_id for the associated EventItem
  public ControllerResponse editEventItem(int eventItemId, int userId)
  {
    ControllerResponse cr = new ControllerResponse();
    
    // begin database related code
    try 
    {
        // try to get db driver class
        try 
        {
            Class.forName(DB_DRIVER_CLASS_NAME);
        } 
        catch (ClassNotFoundException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
  
        // get connection to db
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  
        // sql UPDATE, updates event item as in method description
        String query = "UPDATE event_item SET user_id=? WHERE id=?";  
      
        PreparedStatement stmt = con.prepareStatement(query);
        
        if(userId != -1)
        {
          stmt.setInt(1, userId);
        }
        else
        {
          stmt.setNull(1, Types.INTEGER);
        }
        
        
        stmt.setInt(2, eventItemId);
        
        int rowsAffected = stmt.executeUpdate();
        
        // check to see if event item update occured
        while (rowsAffected < 1) 
        {
          cr.getErrors().add("Error, could update Event Item");
        }
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
    
    return cr;
  }
  
  

  

  // MAPPER FUNCTIONS
  // the following private functions map model objects from ResultSet entries
  // all used to prevent duplication of code
  
  
  // LG
  // mapRowToEvent method assigns event attributes and returns the mapped event object
  private Event mapRowToEvent(ResultSet rs) throws SQLException
  {
    Event event = new Event();
    event.setId(rs.getInt("id"));
    event.setOwningUserId(rs.getInt("owner_user_id"));
    event.setName(rs.getString("name"));
    event.setDescription(rs.getString("description"));
    event.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
    return event;
  }
  
  
  private EventItem mapRowToEventItem(ResultSet rs) throws SQLException
  {
    EventItem eventItem = new EventItem();
    eventItem.setId(rs.getInt("id"));
    eventItem.setUserId(rs.getInt("user_id"));
    eventItem.setEventId(rs.getInt("event_id"));
    eventItem.setName(rs.getString("name"));
    eventItem.setAssignedUserFirstName(rs.getString("first_name"));
    eventItem.setAssignedUserLastName(rs.getString("last_name"));
    return eventItem;
  }
  
  
  private User mapRowToUser(ResultSet rs) throws SQLException
  {
    User user = new User();
    user.setId(rs.getInt("id"));
    user.setFirstName(rs.getString("first_name"));
    user.setLastName(rs.getString("last_name"));
    user.setEmail(rs.getString("email"));
    user.setPassword(rs.getString("password"));
    return user;
  }
  
  
  private EventParticipation mapRowToEventParticipation(ResultSet rs) throws SQLException
  {
    EventParticipation ep = new EventParticipation();
    ep.setId(rs.getInt("id"));
    ep.setEventId(rs.getInt("event_id"));
    ep.setId(rs.getInt("id"));
    return ep;
  }

  // LG
  // getUserEvents method returns list of the user's events
  public ArrayList<Event> getUserEvents(User user) 
  {
	  ArrayList<Event> userEvents = new ArrayList<>();
	  int userId = user.getId();
	  
	  // begin database related code
	  try 
	  {
	      // try to get db driver class
	      try 
	      {
	          Class.forName(DB_DRIVER_CLASS_NAME);
	      } 
	      catch (ClassNotFoundException e) 
	      {
	          // TODO Auto-generated catch block
	          e.printStackTrace();
	      }
	
	      // get connection to db
	      Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	
	      // init SQL query, this will select all events for that user
	      String query = "SELECT events.id, events.owner_user_id, events.name, events.description, events.date_time "
	          + "FROM events "
	          + "LEFT JOIN event_participation ON events.id=event_participation.event_id "
	          + "WHERE event_participation.user_id=?";
	      
	      PreparedStatement stmt = con.prepareStatement(query);
	      
	      stmt.setInt(1, userId);
	      
	      ResultSet rs = stmt.executeQuery();
	      // adds the event mapping to list of events
	      while (rs.next()) 
	      {
	        userEvents.add(mapRowToEvent(rs));
	      }
	    } 
	    catch (SQLException e) 
	    {
	      e.printStackTrace();
	    }
	 
	 return userEvents;
  }

  
    public ArrayList<Event> getEventsNotOwnedByUser(int userId) {
	    ArrayList<Event> events = new ArrayList<>();
	  
	    try {
	        try {
	            Class.forName(DB_DRIVER_CLASS_NAME);
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        
	        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

	        // Define the SQL query to select events not owned by the specified user
	        String query = "SELECT id, owner_user_id, name, description, date_time " +
	                       "FROM events WHERE owner_user_id <> ?";
	        
	        PreparedStatement stmt = con.prepareStatement(query);
	        stmt.setInt(1, userId);

	        ResultSet rs = stmt.executeQuery();
	        
	        // This will Map each row in the result set to an Event object as well as add to the list
	        while (rs.next()) {
	            events.add(mapRowToEvent(rs));
	        }
	        
	        rs.close();
	        stmt.close();
	        con.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return events;
	}
    

    // Updates an event details. (Nas)
    public ControllerResponse updateEvent(Event event) {
    	
    	ControllerResponse cr = new ControllerResponse();
    	
    	 // begin database related code
        try
        {
          // try to get db driver class
          try
          {
            Class.forName(DB_DRIVER_CLASS_NAME);
          }
          catch (ClassNotFoundException e)
          {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          // get connection to db
          Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

          // insert statement
          String statement = "UPDATE events SET name=?, description=?, date_time=? WHERE id=?";

          PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
          

       
          st.setString(1, event.getName());
          st.setString(2, event.getDescription());
          st.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
          st.setInt(4, event.getId());
          
          int affectedRows = st.executeUpdate();
          
          
          
          if(affectedRows <= 0) {
        	  
        	  cr.getErrors().add("Failure: Event could not be updated");
        	  
          }
          // close statement and connection
          st.close();
          con.close();
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {

        }

        
        return cr;
    }

    //getFreeformEventItemsByEventId method returns a list of all freeform event items for a particular Event
    public ArrayList<EventItem> getFreeformEventItemsByEventId(int eventId) 
    {
      ArrayList<EventItem> eventItems = new ArrayList<>();
      
      // begin database related code
      try 
      {
          // try to get db driver class
          try 
          {
              Class.forName(DB_DRIVER_CLASS_NAME);
          } 
          catch (ClassNotFoundException e) 
          {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
    
          // get connection to db
          Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
          // init SQL query, this will select all freeform event items for an event
          String query = "SELECT freeform_event_item.id, freeform_event_item.event_id, freeform_event_item.user_id, freeform_event_item.name, users.first_name, users.last_name "
              + "FROM freeform_event_item "
              + "LEFT JOIN users "
              + "ON freeform_event_item.user_id = users.id WHERE event_id=?";
          
          
          PreparedStatement stmt = con.prepareStatement(query);
          
          stmt.setInt(1, eventId);
          
          ResultSet rs = stmt.executeQuery();
          
          // adds the event items to list of EventItems via mapper function
          while (rs.next()) 
          {
            eventItems.add(mapRowToEventItem(rs));
          }
        } 
        catch (SQLException e) 
        {
          e.printStackTrace();
        }
      
      return eventItems;
    }
    
    // JM
    // deleteFreeformEventItem
    // deletes a freeform event item by an id
    public ControllerResponse deleteFreeformEventItem(int freeformEventItemId)
    {
      ControllerResponse cr = new ControllerResponse();
      
      try 
      {
          // try to get db driver class
          try 
          {
              Class.forName(DB_DRIVER_CLASS_NAME);
          } 
          catch (ClassNotFoundException e) 
          {
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
    
          // get connection to db
          Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    
          // init SQL statement, will delete one freeform event item
          String query = "DELETE FROM freeform_event_item WHERE id=?";
          
          PreparedStatement stmt = con.prepareStatement(query);
          
          // set freeform event item id
          stmt.setInt(1, freeformEventItemId);
          
          // check for errors
          int rowsAffected = stmt.executeUpdate();
          
          if(rowsAffected <= 0)
          {
            cr.getErrors().add("Error, deleting freeform event item failed.");
          }
          
      } 
      catch (SQLException e) 
      {
        e.printStackTrace();
      }
      
      
      
      return cr;
    }
    
    
    // method to insert a list of freeform event items 
    public ControllerResponse insertFreeformEventItems(int eventId, List<EventItem> freeformEventItems)
    {
      ControllerResponse cr = new ControllerResponse();
      
      // begin database related code
      try
      {
        // try to get db driver class
        try
        {
          Class.forName(DB_DRIVER_CLASS_NAME);
        }
        catch (ClassNotFoundException e)

        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }


        // get connection to db
        Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        con.setAutoCommit(false);

        String statement = "INSERT INTO freeform_event_item(event_id, user_id, name) VALUES (?, ?, ?);";
        
        // prepare the statement and set the '?' parameters
        PreparedStatement st = con.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        
        for(EventItem item : freeformEventItems)
        {
          st.setInt(1, eventId);
          
          st.setInt(2, item.getUserId());
       
          st.setString(3, item.getName());
          
          st.addBatch();
        }
        
        
        
        int[] affectedRows = st.executeBatch();
        
        //Explicitly commit statements to apply changes
        con.commit();
         
        // check to see if sum of affectedRows is equal to the number of items passed in
        int sumAffected = 0;
        for(int row : affectedRows)
        {
          sumAffected += row;
        }
        // add the error if applicable
        if(sumAffected != freeformEventItems.size())
        {
          cr.getErrors().add("Error occured when adding event items, could have partially saved some");
        }
        
        
        // close statement and connection
        st.close();
        con.close();
      }
      catch (SQLException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      finally
      {

      }

      
      return cr;
    }
    
    
    
    // handleUpdateEvent dao method, deletes all items related to an event, updates the event details, then reinserts all event items
    public ControllerResponse handleUpdateEvent(Event event, List<EventItem> freeformEventItems, List<EventItem> eventItemList)
    {
      ControllerResponse cr = new ControllerResponse();
      
      Connection con = null;
      // begin database related code
      try
      {
        // try to get db driver class
        try
        {
          Class.forName(DB_DRIVER_CLASS_NAME);
        }
        catch (ClassNotFoundException e)

        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }


        // get connection to db
        con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        con.setAutoCommit(false);
        
        
        // statement strings
        String deleteEventItemString = "DELETE FROM event_item WHERE event_id=?;";
        String deleteFreeformEventItemString = "DELETE FROM freeform_event_item WHERE event_id=?;";
        String updateEventString = "UPDATE events SET name=?, description=?, date_time=? WHERE id=?";

        

     
        
        
        // prepare the statement and set the '?' parameters
        PreparedStatement deleteEventItemStatement = con.prepareStatement(deleteEventItemString, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteFreeformEventItemStatement = con.prepareStatement(deleteFreeformEventItemString, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement updateEventStatement = con.prepareStatement(updateEventString, Statement.RETURN_GENERATED_KEYS);
        
        
        // set params
        
        // delete event items
        deleteEventItemStatement.setInt(1, event.getId());
        
        // delete freeform event items
        deleteFreeformEventItemStatement.setInt(1, event.getId());
        
        // update statment
        updateEventStatement.setString(1, event.getName());
        updateEventStatement.setString(2, event.getDescription());
        updateEventStatement.setTimestamp(3, Timestamp.valueOf(event.getDateTime()));
        updateEventStatement.setInt(4, event.getId());
        
        
        // execute commands
        deleteEventItemStatement.executeUpdate();
        deleteFreeformEventItemStatement.executeUpdate();
        updateEventStatement.executeUpdate();
        
        
        //Explicitly commit statements to apply changes
        con.commit();
         
        
        // perform item inserts, add results to CR error list
        cr.getErrors().addAll(this.insertEventItems(event.getId(), eventItemList).getErrors()); 
        cr.getErrors().addAll(this.insertFreeformEventItems(event.getId(), freeformEventItems).getErrors()); 
        
        
        // close statement and connection
        deleteEventItemStatement.close();
        deleteFreeformEventItemStatement.close();
        updateEventStatement.close();
        con.close();
      }
      catch (SQLException e)
      {
        // TODO Auto-generated catch block
        if(con != null)
        {
          try
          {
            // sql exeception occured, rollback changes
            cr.getErrors().add("Something went wrong, update failed");
            con.rollback();
            con.close();
          }
          catch (SQLException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        
        e.printStackTrace();
      }
      finally
      {

      }

      
      return cr;
    }
    
    
    
    
    
    // handleDeleteEvent method, deletes event items, event participation entries, then event, in that order.
    // essentially deleted the entire event signature from the database.
    public ControllerResponse handleDeleteEvent(int eventId)
    {
      ControllerResponse cr = new ControllerResponse();
      
      Connection con = null;
      // begin database related code
      try
      {
        // try to get db driver class
        try
        {
          Class.forName(DB_DRIVER_CLASS_NAME);
        }
        catch (ClassNotFoundException e)

        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }


        // get connection to db
        con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        con.setAutoCommit(false);
        
        
        // statement strings
        String deleteEventItemString = "DELETE FROM event_item WHERE event_id=?;";
        String deleteFreeformEventItemString = "DELETE FROM freeform_event_item WHERE event_id=?;";
        String deleteEventParticipationString = "DELETE FROM event_participation WHERE event_id=?;";
        String deleteEventString = "DELETE from events WHERE id=?;";

        

     
        
        
        // prepare the statement and set the '?' parameters
        PreparedStatement deleteEventItemStatement = con.prepareStatement(deleteEventItemString, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteFreeformEventItemStatement = con.prepareStatement(deleteFreeformEventItemString, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteEventParticipationStatement = con.prepareStatement(deleteEventParticipationString, Statement.RETURN_GENERATED_KEYS);
        PreparedStatement deleteEventStatement = con.prepareStatement(deleteEventString, Statement.RETURN_GENERATED_KEYS);
        
        
        // set params
        
        // delete event items
        deleteEventItemStatement.setInt(1, eventId);
        
        // delete freeform event items
        deleteFreeformEventItemStatement.setInt(1, eventId);
        
        // delete EPs
        deleteEventParticipationStatement.setInt(1, eventId);
        
        // delete event
        deleteEventStatement.setInt(1, eventId);
        
        // execute commands
        deleteEventItemStatement.executeUpdate();
        deleteFreeformEventItemStatement.executeUpdate();
        deleteEventParticipationStatement.executeUpdate();
        deleteEventStatement.executeUpdate();
        
        
        //Explicitly commit statements to apply changes
        con.commit();
         
        
         
        
        
        // close statement and connection
        deleteEventItemStatement.close();
        deleteFreeformEventItemStatement.close();
        deleteEventParticipationStatement.close();
        deleteEventStatement.close();
        con.close();
      }
      catch (SQLException e)
      {
        // TODO Auto-generated catch block
        if(con != null)
        {
          try
          {
            // sql exeception occured, rollback changes
            cr.getErrors().add("Something went wrong, deletion failed");
            con.rollback();
            con.close();
          }
          catch (SQLException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        
        e.printStackTrace();
      }
      finally
      {

      }

      
      return cr;
    }


}
  
