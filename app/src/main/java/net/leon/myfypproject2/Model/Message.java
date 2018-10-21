package net.leon.myfypproject2.Model;

public class Message {
    private String Date,Message,Sender,Time,Type;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Message(String date, String message, String sender, String time, String type) {
        Date = date;
        Message = message;
        Sender = sender;
        Time = time;
        Type = type;
    }

    public Message() {
    }
}
