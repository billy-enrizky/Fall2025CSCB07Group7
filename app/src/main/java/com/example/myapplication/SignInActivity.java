package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private Button signInButton;
    private Button forgotPasswordButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = findViewById(R.id.sign_in_button);
        forgotPasswordButton = findViewById(R.id.sign_in_forgot_password);
        emailEditText = findViewById(R.id.sign_in_email);
        passwordEditText = findViewById(R.id.sign_in_password);

        mAuth = FirebaseAuth.getInstance();

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserManager.currentUser = new UserData();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void signInClick(View view) {
        mAuth.signInWithEmailAndPassword(
                emailEditText.getText().toString().trim(),
                passwordEditText.getText().toString().trim()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                    UserManager.currentUser.ReadFromDatabase(mDatabase, mAuth.getCurrentUser(), new CallBack() {
                        @Override
                        public void onComplete() {

                            if (UserManager.currentUser.firstTime == true) {

                                // First-time users go to onboarding
                                Intent intent1 = new Intent(SignInActivity.this, OnBoardingActivity.class);
                                startActivity(intent1);

                            } else {

                                // Returning users go to CHILD HOME SCREEN
                                Intent intent = new Intent(SignInActivity.this, ChildHomeActivity.class);
                                startActivity(intent);
                                finish(); // prevents going back to sign-in
                            }
                        }
                    });

                } else {
                    Toast.makeText(SignInActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void CreateNewAccount(View view) {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
