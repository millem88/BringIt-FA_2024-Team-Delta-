/// The EventItem class represents an items that are related to an event.
/// It will hold information about the item, the associated event and the assigned user.
/// Author(s): Darien Dalton.
/// Date: 09/23/2024

package models;

public class EventItem {
	private int id;
    private int eventId;
    private int userId;
    private String name;
    private String assignedUserFirstName;
    private String assignedUserLastName;

    public EventItem() {
        this.id = -1;
        this.eventId = -1;
        this.userId = -1;
        this.name = "";
        this.assignedUserFirstName = "";
        this.assignedUserLastName = "";
    }

    
    public EventItem(int id, int eventId, int userId, String name, String assignedUserFirstName, String assignedUserLastName)
    {
      this.id = id;
      this.eventId = eventId;
      this.userId = userId;
      this.name = name;
      this.assignedUserFirstName = assignedUserFirstName;
      this.assignedUserLastName = assignedUserLastName;
    }

    public EventItem(int id, int eventId, int userId, String name) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.name = name;
    } 
    
    //getter and setter for 'assignedUserFirstName'
    public String getAssignedUserFirstName()
    {
      return assignedUserFirstName;
    }

    public void setAssignedUserFirstName(String assignedUserFirstName)
    {
      this.assignedUserFirstName = assignedUserFirstName;
    }

  //getter and setter for 'assignedUserLastName'
    public String getAssignedUserLastName()
    {
      return assignedUserLastName;
    }

    public void setAssignedUserLastName(String assignedUserLastName)
    {
      this.assignedUserLastName = assignedUserLastName;
    }

  //getter and setter for 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  //getter and setter for 'eventId'
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

  //getter and setter for 'userId'
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

  //getter and setter for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //toString in order to display event items in a readable manner
    @Override
    public String toString() {
        return "EventItem [id=" + id + ", eventId=" + eventId + ", userId=" + userId + ", name=" + name + "]";
    }
}

