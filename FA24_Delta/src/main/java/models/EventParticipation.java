/// The EventParticipation class will track a user's participation in an event.
/// It will link the user to a specific event and track who is attending or involved 
/// in each of the events.
/// Author(s): Darien Dalton.
/// Date: 09/23/2024

package models;

public class EventParticipation {
	private int id;
    private int userId;
    private int eventId;

    public EventParticipation() {
        this.id = -1;
        this.userId = -1;
        this.eventId = -1;
    }

    public EventParticipation(int id, int userId, int eventId) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
    }

    //getter and setter for 'id'
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //getter and setter for 'userId'
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    //getter and setter for 'eventId'
    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    //toString in order to display participation details in a readable manner.
    @Override
    public String toString() {
        return "EventParticipation [id=" + id + ", userId=" + userId + ", eventId=" + eventId + "]";
    }
}

