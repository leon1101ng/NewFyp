package net.leon.myfypproject2.Model;

public class Plan {
    private String Date,PlanName,Time,Uid,Username,UserImage;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPlanName() {
        return PlanName;
    }

    public void setPlanName(String planName) {
        PlanName = planName;
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

    public Plan(String date, String planName, String time, String uid, String username, String userImage) {
        Date = date;
        PlanName = planName;
        Time = time;
        Uid = uid;
        Username = username;
        UserImage = userImage;
    }

    public Plan() {
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
