package net.leon.myfypproject2.Model;

public class ImageCommentClass {
   private String Comment;
   private String Date;
    private   String Time;
    private   String Uid;
    private String Username;
    public ImageCommentClass(){}

    public ImageCommentClass(String comment, String date, String time, String uid, String username) {
        Comment = comment;
        Date = date;
        Time = time;
        Uid = uid;
        Username = username;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }







}
