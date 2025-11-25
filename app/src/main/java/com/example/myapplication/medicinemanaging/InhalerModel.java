package com.example.myapplication.medicinemanaging;

import androidx.annotation.NonNull;

import com.example.myapplication.CallBack;
import com.example.myapplication.ResultCallBack;
import com.example.myapplication.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class InhalerModel {
    //Create or modify an inhaler
    //Create is inhaler.InhalerID is null or equals ""
    //Modify if inhaler.InhalerID is not null and not equals ""

    public static void writeIntoDB(Inhaler inhaler, CallBack callback){
        String ParentId = inhaler.parentsID;
        DatabaseReference Ref = UserManager.mDatabase.child("InhalerManager").child(ParentId).getRef();
        if(inhaler.InhalerID == null || inhaler.InhalerID.equals("")){
            String nodeID = Ref.push().getKey();
            inhaler.InhalerID = nodeID;
        }
        Ref.child(inhaler.InhalerID).setValue(inhaler).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if(callback != null) {
                    callback.onComplete();
                }
            }
        });
    }

    public static void ReadFromDatabase(String ParentId, ResultCallBack<HashMap<String, Inhaler>> callback) {
        UserManager.mDatabase.child("InhalerManager").child(ParentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Inhaler> inhalercollection = new HashMap<>();
                        for (DataSnapshot s : snapshot.getChildren()) {
                            String key = s.getKey();
                            Inhaler Inhaler = s.getValue(Inhaler.class);
                            inhalercollection.put(key, Inhaler);
                        }
                        if (callback != null){
                            callback.onComplete(inhalercollection);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    public static void DeleteFromDatabase(Inhaler inhaler, CallBack callback){
        String ParentID = inhaler.parentsID;
        String InhalerID = inhaler.InhalerID;
        UserManager.mDatabase.child("InhalerManager").child(ParentID).child(InhalerID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(callback!= null){
                    callback.onComplete();
                }
            }
        });
    }
}
