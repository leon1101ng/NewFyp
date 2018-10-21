package net.leon.myfypproject2.Model;

public class FanMessage {
    private String Date,Fullname,Message,ProfilePicture,Time,User,Username;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public FanMessage(){}

    public FanMessage(String date, String fullname, String message, String profilePicture, String time, String user, String username) {
        Date = date;
        Fullname = fullname;
        Message = message;
        ProfilePicture = profilePicture;
        Time = time;
        User = user;
        Username = username;
    }
}
