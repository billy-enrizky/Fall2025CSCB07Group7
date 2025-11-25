package com.example.myapplication.medicinemanaging;

import androidx.annotation.NonNull;

import com.example.myapplication.CallBack;
import com.example.myapplication.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class InhalerModel {
    public static void writeIntoDB(inhaler inhaler, CallBack callback){
        String ParentId = inhaler.parentsID;
        UserManager.mDatabase.child("InhalerManager").child(ParentId).setValue(inhaler).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (callback != null) {
                    callback.onComplete();
                }
            }
        });
    }
}
