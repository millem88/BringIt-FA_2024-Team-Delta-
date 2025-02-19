package models;

import java.util.ArrayList;


/// ControllerResponse class, used to hold data from various operations with the purpose of returning it to the frontend for response checking.
/// Data within that class can be added (meaning instance variables/getters/setters can be added), but do not remove any existing variables.
/// Author(s): Jamie Mizelle, Lisa Gehrt
/// Date: 9/29/24
public class ControllerResponse
{
  private User user;
  private ArrayList<String> errors;
  private int lastInsertedId;
  private ArrayList<Event> events;
  private String eventCase;

  
  public ControllerResponse()
  {
    this.user = new User();
    this.errors = new ArrayList<String>();
    this.lastInsertedId = -1;
    this.events = new ArrayList<Event>();
    this.eventCase = "";
  }

  public String getEventCase()
  {
    return eventCase;
  }

  public void setEventCase(String eventCase)
  {
    this.eventCase = eventCase;
  }

  public ArrayList<String> getErrors()
  {
    return errors;
  }

  public int getLastInsertedId()
  {
    return lastInsertedId;
  }

  public void setLastInsertedId(int lastInsertedId)
  {
    this.lastInsertedId = lastInsertedId;
  }

  public void setErrors(ArrayList<String> errors)
  {
    this.errors = errors;
  }

  public User getUser()
  {
    return user;
  }

  public void setUser(User user)
  {
    this.user = user;
  }
  
  //LG - returns list of events
  public ArrayList<Event> getEvents() {
	  return events;
  }
  
  //LG - sets list of events
  public void setEvents(ArrayList<Event> events) {
	  this.events = events;
  }
  
}
