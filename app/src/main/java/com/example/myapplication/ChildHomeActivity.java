package com.example.myapplication;

import android.content.Intent;
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
        Button inhalerBtn = findViewById(R.id.btnInhalerHelper);

        logSymptoms.setOnClickListener(v -> {
            // TODO: navigate to symptom logging screen
        });

        logMedicine.setOnClickListener(v -> {
            // TODO: navigate to medicine logging screen
        });

        // ⭐ Navigate to inhaler technique screen
        inhalerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChildHomeActivity.this, InhalerTechniqueActivity.class);
            startActivity(intent);
        });
    }
}
