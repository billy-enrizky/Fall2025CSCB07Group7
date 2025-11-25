package com.example.myapplication.achievementmanaging;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.CallBack;
import com.example.myapplication.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

public class AchievementsModel {

    public static void WriteIntoDB(Achievement achievement, CallBack callback){
        String ChildId = achievement.ChildID;
        UserManager.mDatabase.child("Achievements").child(ChildId).setValue(achievement).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });
    }
    public static void ReadFromDatabase(String ChildId, CallBack callback) {
        UserManager.mDatabase.child("Achievements").child(ChildId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
        }
    }

}
