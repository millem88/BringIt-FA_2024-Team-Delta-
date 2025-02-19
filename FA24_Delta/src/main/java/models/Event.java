/// The Event class represents an event and contains details about the event's 
/// owner, name, the description and data/time of the event. It will be used
/// for things like scheduling or event management. 
/// Author(s): Darien Dalton.
/// Date: 09/23/2024

package models;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

// Event class creates an event object
// Author(s): Lisa Gehrt

public class Event {
	private int id; 
    private int owningUserId; 
    private String name;
    private String description;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  // This pattern matches the format submitted by `datetime-local`
    private LocalDateTime dateTime;

    public Event() {
        this.id = -1;
        this.owningUserId = -1;
        this.name = "";
        this.description = "";
        this.dateTime = LocalDateTime.now();
    }

    public Event(int id, int owningUserId, String name, String description, LocalDateTime dateTime) {
        this.id = id;
        this.owningUserId = owningUserId;
        this.name = name;
        this.description = description;
        this.dateTime = dateTime;
    }
    
    //getters and setters for 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

  //getters and setters for 'owningUserid'
    public int getOwningUserId() {
        return owningUserId;
    }

    public void setOwningUserId(int owningUserId) {
        this.owningUserId = owningUserId;
    }

  //getters and setters for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  //getters and setters for 'description'
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

  //getters and setters for 'dateTime'
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    //toString used to display event details in a readable manner
    @Override
    public String toString() {
        return "Event [id=" + id + ", owningUserId=" + owningUserId + ", name=" + name + ", description=" + description + ", dateTime=" + dateTime + "]";
    }
}

