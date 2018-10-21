package net.leon.myfypproject2.Model;

public class UserClass {
    private String username,ProfilePicture,Fullname,UserID;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public UserClass(String username, String profilePicture, String fullname, String userID) {
        this.username = username;
        ProfilePicture = profilePicture;
        Fullname = fullname;
        UserID = userID;
    }

    public UserClass(){}

}
