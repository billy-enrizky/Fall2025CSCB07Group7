package com.example.myapplication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.time.ZoneId;
public class RescueLog {
    String username;
    long timestamp;
    String feeling;
    int rating;

    public RescueLog(){
        this.feeling = "Better";
        this.rating = 1;
        this.timestamp = System.currentTimeMillis();
    }
    public RescueLog(String username, String feeling, int rating){
        this.username = username;
        this.feeling = feeling;
        this.rating = rating;
        this.timestamp = System.currentTimeMillis();
    }
    public String getDate(){
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(this.timestamp / 1000, 0, ZoneOffset.UTC);
        dateTime = dateTime.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        return dateTime.format(formatter);
    }
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public long getTimestamp() {return timestamp;}
    public void setTimestamp(long timestamp) {this.timestamp = timestamp;}
    public String getFeeling() {return feeling;}
    public void setFeeling(String feeling) {this.feeling = feeling;}
    public int getRating() {return rating;}
    public void setRating(int rating) {this.rating = rating;}
}
