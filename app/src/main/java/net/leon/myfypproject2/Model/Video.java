package net.leon.myfypproject2.Model;

public class Video {
    private String UserFullname,UserID,UserPicture,Username,VideoCaption,VideoLocation,VideoUrl;
    public Video() {
    }

    public String getUserFullname() {
        return UserFullname;
    }

    public void setUserFullname(String userFullname) {
        UserFullname = userFullname;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getVideoCaption() {
        return VideoCaption;
    }

    public void setVideoCaption(String videoCaption) {
        VideoCaption = videoCaption;
    }

    public String getVideoLocation() {
        return VideoLocation;
    }

    public void setVideoLocation(String videoLocation) {
        VideoLocation = videoLocation;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public Video(String userFullname, String userID, String userPicture, String username, String videoCaption, String videoLocation, String videoUrl) {
        UserFullname = userFullname;
        UserID = userID;
        UserPicture = userPicture;
        Username = username;
        VideoCaption = videoCaption;
        VideoLocation = videoLocation;
        VideoUrl = videoUrl;
    }


}
