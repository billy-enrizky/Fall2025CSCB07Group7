package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ChildHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_home);

        Button logSymptoms = findViewById(R.id.btnLogSymptoms);
        Button logMedicine = findViewById(R.id.btnLogMedicine);

        logSymptoms.setOnClickListener(v -> {
            // TODO: Navigate to symptom logging
        });

        logMedicine.setOnClickListener(v -> {
            // TODO: Navigate to medicine logging
        });
    }
}
