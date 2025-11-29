package com.example.myapplication;

public class Inhaler {
    public String username;
    public long datePurchased;
    public long dateExpiry;
    boolean isRescue;

    final long DATEEXPIRYTHRESHOLD = 604800000L;//7 days in milliseconds

    final double PERCENTEMPTYTHRESHOLD = 0.25; //25% or lower I guess

    final int SPRAYTOMLMULT = 10; //10 sprays is 1 ml (I googled)
    public String parentID;
    int maxcapacity;
    private int spraycount;

    public Inhaler(){}

    public Inhaler(long datePurchased, long dateExpiry, String parentID, int maxcapacity, int spraycount){
        this.datePurchased = datePurchased;
        this.dateExpiry = dateExpiry;
        this.parentID = parentID;
        this.maxcapacity = maxcapacity;
        this.spraycount = spraycount;
    }

    public boolean checkExpiry(long todayDate){
        return (todayDate - this.dateExpiry < DATEEXPIRYTHRESHOLD);
    }

    public boolean checkEmpty(){
        return (this.maxcapacity - SPRAYTOMLMULT*this.spraycount < this.maxcapacity * PERCENTEMPTYTHRESHOLD);
    }

    public void use(){
        this.spraycount++;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
           this.username = username;
    }
    public long getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(long datePurchased) {
        this.datePurchased = datePurchased;
    }

    public long getDateExpiry() {
        return dateExpiry;
    }

    public void setDateExpiry(long dateExpiry) {
        this.dateExpiry = dateExpiry;
    }

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public int getMaxcapacity() {
        return maxcapacity;
    }

    public void setMaxcapacity(int maxcapacity) {
        this.maxcapacity = maxcapacity;
    }
    public int getSpraycount() {
        return spraycount;
    }
    public void setSpraycount(int spraycount) {
        this.spraycount = spraycount;
    }
}