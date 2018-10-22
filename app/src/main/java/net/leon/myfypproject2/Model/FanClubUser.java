package net.leon.myfypproject2.Model;

public class FanClubUser {
    private  String UserImage,Username,UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    private int Vip;

    public int getVip() {
        return Vip;
    }

    public void setVip(int vip) {
        Vip = vip;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public FanClubUser(String userImage, String username, String userID, int vip) {
        UserImage = userImage;
        Username = username;
        UserID = userID;
        Vip = vip;
    }

    public FanClubUser() {
    }
}
