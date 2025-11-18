package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int Type = 0;
        if(UserManager.mAuth.getCurrentUser() == null){
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
        if(UserManager.currentUser.account == accountType.CHILD){
            Type = 1;
        }else if(UserManager.currentUser.account == accountType.PARENT){
            Type = 2;
        }else{
            Type = 3;
        }
        // adjust type of currentUser to corresponding role.
        if(Type == 1){
            UserManager.currentUser = new Child();
            UserManager.currentUser.ReadFromDatabase(UserManager.mDatabase, UserManager.mAuth.getCurrentUser(), new CallBack(){
                @Override
                public void onComplete(){
                    Intent intent = new Intent(MainActivity.this, ChildrenActivity.class);
                    startActivity(intent);
                }
            });
        }else if(Type == 2){
            UserManager.currentUser = new Parents();
            UserManager.currentUser.ReadFromDatabase(UserManager.mDatabase, UserManager.mAuth.getCurrentUser(), new CallBack(){
                @Override
                public void onComplete(){
                    Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            UserManager.currentUser = new Providers();
            UserManager.currentUser.ReadFromDatabase(UserManager.mDatabase, UserManager.mAuth.getCurrentUser(), new CallBack(){
                @Override
                public void onComplete(){
                    Intent intent = new Intent(MainActivity.this, ProvidersActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void Onclick(android.view.View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");
    }
    public void GoToSignUp(android.view.View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
    public void GoToSignIn(android.view.View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}