package net.leon.myfypproject2.Model;

public class FanClubChatClass {
    private String Chat;
    private String ChatDate;
    private String ChatTime;
    private String Username;

    public FanClubChatClass(){}

    public String getChat() {
        return Chat;
    }

    public void setChat(String chat) {
        Chat = chat;
    }

    public String getChatDate() {
        return ChatDate;
    }

    public void setChatDate(String chatDate) {
        ChatDate = chatDate;
    }

    public String getChatTime() {
        return ChatTime;
    }

    public void setChatTime(String chatTime) {
        ChatTime = chatTime;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public FanClubChatClass(String chat, String chatDate, String chatTime, String username) {
        Chat = chat;
        ChatDate = chatDate;
        ChatTime = chatTime;
        Username = username;
    }


}
