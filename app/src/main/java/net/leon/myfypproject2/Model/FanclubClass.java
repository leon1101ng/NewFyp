package net.leon.myfypproject2.Model;

public class FanclubClass {
    private String ClubCreator;
    private String ClubName;
    private String ClubPicture;
    private String ClubTopic;

    public FanclubClass(){}

    public String getClubCreator() {
        return ClubCreator;
    }

    public void setClubCreator(String clubCreator) {
        ClubCreator = clubCreator;
    }

    public String getClubName() {
        return ClubName;
    }

    public void setClubName(String clubName) {
        ClubName = clubName;
    }

    public String getClubPicture() {
        return ClubPicture;
    }

    public void setClubPicture(String clubPicture) {
        ClubPicture = clubPicture;
    }

    public String getClubTopic() {
        return ClubTopic;
    }

    public void setClubTopic(String clubTopic) {
        ClubTopic = clubTopic;
    }

    public FanclubClass(String clubCreator, String clubName, String clubPicture, String clubTopic) {
        ClubCreator = clubCreator;
        ClubName = clubName;
        ClubPicture = clubPicture;
        ClubTopic = clubTopic;
    }
}
