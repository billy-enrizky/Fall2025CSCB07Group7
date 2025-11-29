package com.example.myapplication;

public class Achievement {
    String ChildID ;
    Long TimeSinceLastDose;
    int NumberOfConsecutiveDay;
    Badge Badge1;
    Badge Badge2;
    Badge Badge3;
    Badge Badge4;
    Badge Badge5;
    //To have setValue getValue work, empty constructor, getter and setter are required
    //below are empty constructor
    public Achievement(){

    }
    // Below are getter/setter
    public String getChildID() {
        return ChildID;
    }

    public void setChildID(String childID) {
        this.ChildID = childID;
    }

    public Long getTimeSinceLastDose() {
        return TimeSinceLastDose;
    }

    public void setTimeSinceLastDose(Long timeSinceLastDose) {
        this.TimeSinceLastDose = timeSinceLastDose;
    }

    public int getNumberOfConsecutiveDay() {
        return NumberOfConsecutiveDay;
    }

    public void setNumberOfConsecutiveDay(int numberOfConsecutiveDay) {
        this.NumberOfConsecutiveDay = numberOfConsecutiveDay;
    }

    public Badge getBadge1() {
        return Badge1;
    }

    public void setBadge1(Badge badge1) {
        this.Badge1 = badge1;
    }

    public Badge getBadge2() {
        return Badge2;
    }

    public void setBadge2(Badge badge2) {
        this.Badge2 = badge2;
    }

    public Badge getBadge3() {
        return Badge3;
    }

    public void setBadge3(Badge badge3) {
        this.Badge3 = badge3;
    }

    public Badge getBadge4() {
        return Badge4;
    }

    public void setBadge4(Badge badge4) {
        this.Badge4 = badge4;
    }

    public Badge getBadge5() {
        return Badge5;
    }

    public void setBadge5(Badge badge5) {
        this.Badge5 = badge5;
    }

}
