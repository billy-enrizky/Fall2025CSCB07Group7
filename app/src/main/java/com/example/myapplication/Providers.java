package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class Providers extends UserData {
    List<Parents> LinkedParents;
    public Providers(){
        super();
        LinkedParents = new ArrayList<>();
    }
    public Providers(String ID, String Email, accountType account){
        super(ID, Email, account);
        LinkedParents = new ArrayList<>();
    }

}
