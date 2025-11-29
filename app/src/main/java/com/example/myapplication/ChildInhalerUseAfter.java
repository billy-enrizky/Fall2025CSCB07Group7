package com.example.myapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.userdata.ChildAccount;

public class ChildInhalerUseAfter extends AppCompatActivity {
    ChildAccount currentUser;
    ControllerLog controllerLog = new ControllerLog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        controllerLog.setFeelingB(intent.getStringExtra("feelrating"));
        controllerLog.setRatingB(intent.getIntExtra("breathrating", 1));
        controllerLog.setUsername(UserManager.currentUser.getID());
        setContentView(R.layout.activity_inhaler_use_after);

        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controllerLog.setRatingA(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button happyButton = findViewById(R.id.happybutton);
        Button neutralButton = findViewById(R.id.neutralbutton);
        Button sadButton = findViewById(R.id.sadbutton);
        Button confirmButton = findViewById(R.id.confirmbutton);

        happyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyButton.setBackgroundTintList(ColorStateList.valueOf(0xFF94D95F));
                neutralButton.setBackgroundTintList(ColorStateList.valueOf(0xFFFFD498));
                sadButton.setBackgroundTintList(ColorStateList.valueOf(0xFFFF7B7B));
                controllerLog.setFeelingA("Better");
            }
        });

        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyButton.setBackgroundTintList(ColorStateList.valueOf(0xFFAEFF70));
                neutralButton.setBackgroundTintList(ColorStateList.valueOf(0xFFD8B481));
                sadButton.setBackgroundTintList(ColorStateList.valueOf(0xFFFF7B7B));
                controllerLog.setFeelingA("Same");
            }
        });

        sadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happyButton.setBackgroundTintList(ColorStateList.valueOf(0xFFAEFF70));
                neutralButton.setBackgroundTintList(ColorStateList.valueOf(0xFFFFD498));
                sadButton.setBackgroundTintList(ColorStateList.valueOf(0xFFD86868));
                controllerLog.setFeelingA("Worse");
            }
        });

        findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChildInhalerUseAfter.this, ChildInhalerUseReady.class));
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ControllerLogModel.writeIntoDB(controllerLog, new CallBack() {
                    @Override
                    public void onComplete() {
                        startActivity(new Intent(ChildInhalerUseAfter.this, ChildInhalerMenu.class));
                    }
                });
            }
        });
    }
}