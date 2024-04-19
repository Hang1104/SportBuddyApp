package my.edu.utar.assignment2.ProfilePage;

import java.util.Date;

public class GameRecord {
    private String sportType;
    private String gameSkill;
    private String location;
    private Date date;
    private String startTime;
    private String endTime;
    private boolean hasGameDatePassed;

    public GameRecord(String sportType, String gameSkill, String location, Date date, String startTime, String endTime, boolean hasGameDatePassed) {
        this.sportType = sportType;
        this.gameSkill = gameSkill;
        this.location = location;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasGameDatePassed = hasGameDatePassed;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getGameSkill() {
        return gameSkill;
    }

    public void setGameSkill(String gameSkill) {
        this.gameSkill = gameSkill;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean hasGameDatePassed() {
        Date currentDate = new Date();
        return currentDate.after(date);
    }
}

