package com.example.myapplication.dailycheckin;

import com.example.myapplication.UserManager;
import com.example.myapplication.userdata.AccountType;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class DailyCheckin {
    String username;
    AccountType loggedBy;
    boolean nightWaking;
    String activityLimits;
    double coughWheezeLevel;
    ArrayList<String> triggers;
    String yet_to_implement;
    // mind that, to have setValue(this) works, a pair of getter setter is needed for each field
    // a constructor of empty parameter is also needed.
    public DailyCheckin(String username, boolean nightWaking, String activityLimits, double coughWheezeLevel, ArrayList<String>triggers) {
        this.username = username;
        this.loggedBy = UserManager.currentUser.getAccount();
        this.nightWaking = nightWaking;
        this.activityLimits = activityLimits;
        this.coughWheezeLevel = coughWheezeLevel;
        this.triggers = triggers;
    }
    public String getUsername(){ // format: getName, mind the upper case
        return username;
    }
    public void setUsername(String username){ // setName
        this.username = username;
    }
    public AccountType getLoggedBy() {
        return this.loggedBy;
    }
    public void setLoggedBy(AccountType loggedBy) {
        this.loggedBy = loggedBy;
    }
    public String getActivityLimits() {
        return this.activityLimits;
    }
    public void setActivityLimits(String activityLimits) {
        this.activityLimits=activityLimits;
    }
    public double getCoughWheezeLevel() {
        return this.coughWheezeLevel;
    }
    public void setCoughWheezeLevel(double coughWheezeLevel) {
        this.coughWheezeLevel = coughWheezeLevel;
    }
    public ArrayList<String>getTriggers() {
        return this.triggers;
    }
    public void setTriggers(ArrayList<String> triggers) {
        this.triggers = triggers;
    }

}
