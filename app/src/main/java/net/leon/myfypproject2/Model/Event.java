package net.leon.myfypproject2.Model;

public class Event {
    private String Event_Description,Event_EndDate,Event_EndTime,Event_Location,Event_Payment,Event_People,Event_Price,Event_StartDate,Event_StartTime,Event_Title,Username;


    public Event(){}

    public String getEvent_Description() {
        return Event_Description;
    }

    public void setEvent_Description(String event_Description) {
        Event_Description = event_Description;
    }

    public String getEvent_EndDate() {
        return Event_EndDate;
    }

    public void setEvent_EndDate(String event_EndDate) {
        Event_EndDate = event_EndDate;
    }

    public String getEvent_EndTime() {
        return Event_EndTime;
    }

    public void setEvent_EndTime(String event_EndTime) {
        Event_EndTime = event_EndTime;
    }

    public String getEvent_Location() {
        return Event_Location;
    }

    public void setEvent_Location(String event_Location) {
        Event_Location = event_Location;
    }

    public String getEvent_Payment() {
        return Event_Payment;
    }

    public void setEvent_Payment(String event_Payment) {
        Event_Payment = event_Payment;
    }

    public String getEvent_People() {
        return Event_People;
    }

    public void setEvent_People(String event_People) {
        Event_People = event_People;
    }

    public String getEvent_Price() {
        return Event_Price;
    }

    public void setEvent_Price(String event_Price) {
        Event_Price = event_Price;
    }

    public String getEvent_StartDate() {
        return Event_StartDate;
    }

    public void setEvent_StartDate(String event_StartDate) {
        Event_StartDate = event_StartDate;
    }

    public String getEvent_StartTime() {
        return Event_StartTime;
    }

    public void setEvent_StartTime(String event_StartTime) {
        Event_StartTime = event_StartTime;
    }

    public String getEvent_Title() {
        return Event_Title;
    }

    public void setEvent_Title(String event_Title) {
        Event_Title = event_Title;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Event(String event_Description, String event_EndDate, String event_EndTime, String event_Location, String event_Payment, String event_People, String event_Price, String event_StartDate, String event_StartTime, String event_Title, String username) {
        Event_Description = event_Description;
        Event_EndDate = event_EndDate;
        Event_EndTime = event_EndTime;
        Event_Location = event_Location;
        Event_Payment = event_Payment;
        Event_People = event_People;
        Event_Price = event_Price;
        Event_StartDate = event_StartDate;
        Event_StartTime = event_StartTime;
        Event_Title = event_Title;
        Username = username;
    }
}
