package net.leon.myfypproject2.Model;

public class UserPostsImage {
    private String Date;
    private String Fullname;
    private String ImageCaption;
    private String ImagePostLocation;

    public String getImagePostLocation() {
        return ImagePostLocation;
    }

    public void setImagePostLocation(String imagePostLocation) {
        ImagePostLocation = imagePostLocation;
    }

    private String ImageUrl;
    private String PostImage;
    private String Time;
    private String UserID;
    private String Username;

    public UserPostsImage(String date, String fullname, String imageCaption, String imagePostLocation, String imageUrl, String postImage, String time, String userID, String username) {
        Date = date;
        Fullname = fullname;
        ImageCaption = imageCaption;
        ImagePostLocation = imagePostLocation;
        ImageUrl = imageUrl;
        PostImage = postImage;
        Time = time;
        UserID = userID;
        Username = username;
    }

    public UserPostsImage(){}

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

    public String getImageCaption() {
        return ImageCaption;
    }

    public void setImageCaption(String imageCaption) {
        ImageCaption = imageCaption;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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
}

