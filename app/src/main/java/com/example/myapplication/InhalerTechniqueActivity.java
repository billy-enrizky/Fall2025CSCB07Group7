package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class InhalerTechniqueActivity extends AppCompatActivity {

    private VideoView video;
    private TextView stepText;
    private Button nextStep, backStep;

    private int currentStep = 0;

    private final String[] steps = {
            "Step 1: Shake the inhaler well.",
            "Step 2: Breathe out fully.",
            "Step 3: Put the inhaler in your mouth.",
            "Step 4: Press inhaler once as you breathe in slowly.",
            "Step 5: Hold your breath for 10 seconds.",
            "Step 6: Breathe out slowly."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhaler_technique);

        video = findViewById(R.id.inhalerVideo);
        stepText = findViewById(R.id.stepText);
        nextStep = findViewById(R.id.nextStepButton);
        backStep = findViewById(R.id.backStepButton);

        // Load demo video (place a video called inhaler_demo.mp4 in res/raw)
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.inhaler_demo);
        video.setVideoURI(videoUri);
        video.start();

        stepText.setText(steps[currentStep]);

        nextStep.setOnClickListener(v -> {
            if (currentStep < steps.length - 1) {
                currentStep++;
                stepText.setText(steps[currentStep]);
            }
        });

        backStep.setOnClickListener(v -> {
            if (currentStep > 0) {
                currentStep--;
                stepText.setText(steps[currentStep]);
            }
        });
    }
}
