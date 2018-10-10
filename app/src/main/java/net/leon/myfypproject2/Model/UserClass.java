package net.leon.myfypproject2.Model;

public class UserClass {
    private String username;
    private String ProfilePicture;
    private String Fullname;
    private String UserID;

    public String getInAppCredit() {
        return InAppCredit;
    }

    public void setInAppCredit(String inAppCredit) {
        InAppCredit = inAppCredit;
    }

    private String InAppCredit;




    public UserClass(){}
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

    public UserClass(String username, String profilePicture, String fullname, String userID, String inAppCredit) {
        this.username = username;
        ProfilePicture = profilePicture;
        Fullname = fullname;
        UserID = userID;

        InAppCredit = inAppCredit;
    }


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
