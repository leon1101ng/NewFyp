package net.leon.myfypproject2.Model;

public class Status {
    private String Fullname,LocationStatusView,ProfilePicture,Status,TagStatusView,UserID,Username;

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getLocationStatusView() {
        return LocationStatusView;
    }

    public void setLocationStatusView(String locationStatusView) {
        LocationStatusView = locationStatusView;
    }

    public String getProfilePicture() {
        return ProfilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        ProfilePicture = profilePicture;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTagStatusView() {
        return TagStatusView;
    }

    public void setTagStatusView(String tagStatusView) {
        TagStatusView = tagStatusView;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Status(String fullname, String locationStatusView, String profilePicture, String status, String tagStatusView, String userID, String username) {
        Fullname = fullname;
        LocationStatusView = locationStatusView;
        ProfilePicture = profilePicture;
        Status = status;
        TagStatusView = tagStatusView;
        UserID = userID;
        Username = username;
    }

    public Status() {
    }
}
