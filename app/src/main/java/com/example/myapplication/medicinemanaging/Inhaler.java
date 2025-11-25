package com.example.myapplication.medicinemanaging;

public class Inhaler {
    String parentsID;
    String InhalerID;
    //To have setValue getValue work, empty constructor, getter and setter are required
    public Inhaler() {

    }
    public void setParentsID(String parentsID) {
        this.parentsID = parentsID;
    }
    public void setInhalerID(String inhalerID) {
        this.InhalerID = inhalerID;
    }
    public String getParentsID() {
        return parentsID;
    }
    public String getInhalerID() {
        return InhalerID;
    }
    // Rest components are to implemented by Terry
}
