package my.edu.utar.assignment2.createGame;

import java.io.Serializable;

public class Game {
    private String sportType;
    private String location;
    private String address;
    private String date;
    private String startTime;
    private String endTime;
    private String gameSkill;
    private String userId;
    private long createdTime;
    private int maxPlayers;

    // Default constructor (required by Firebase Firestore)
    public Game() {
        // Default constructor required by Firebase Firestore
    }

    // Constructor
    public Game(String sportType, String location, String address, String date, String startTime, String endTime, String gameSkill, String userId, long createdTime, int maxPlayers) {
        this.sportType = sportType;
        this.location = location;
        this.address = address;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.gameSkill = gameSkill;
        this.userId = userId;
        this.createdTime = createdTime;
        this.maxPlayers = maxPlayers;
    }

    // Getters and setters
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getGameSkill() {
        return gameSkill;
    }

    public void setGameSkill(String gameSkill) {
        this.gameSkill = gameSkill;
    }

}

