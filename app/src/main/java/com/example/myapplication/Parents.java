package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Parents extends UserData {
    List<Providers> LinkedProviders;
    List<Child> LinkedChildren;
    public Parents(){
        super();
        LinkedProviders = new ArrayList<>();
        LinkedChildren = new ArrayList<>();
    }
    public Parents(String ID, String Email, accountType account){
        super(ID, Email, account);
        LinkedProviders = new ArrayList<>();
        LinkedChildren = new ArrayList<>();
    }
}
