package com.example.myapplication;

public class Child extends UserData {
    Parents LinkedParent;
    public Child(){
        super();
        LinkedParent = new Parents();
    }
    public Child(String ID, String Email, accountType account){
        super(ID, Email, account);
    }
}