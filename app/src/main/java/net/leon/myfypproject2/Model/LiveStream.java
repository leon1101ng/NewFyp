package net.leon.myfypproject2.Model;

public class LiveStream {
    private String Fullname,LiveTitle,PostImage,Status,Username;

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getLiveTitle() {
        return LiveTitle;
    }

    public void setLiveTitle(String liveTitle) {
        LiveTitle = liveTitle;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public LiveStream(String fullname, String liveTitle, String postImage, String status, String username) {
        Fullname = fullname;
        LiveTitle = liveTitle;
        PostImage = postImage;
        Status = status;
        Username = username;
    }

    public LiveStream() {
    }
}
