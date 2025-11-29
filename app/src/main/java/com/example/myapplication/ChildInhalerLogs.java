package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.TypedValue;
import android.graphics.Color;
import java.util.ArrayList;

public class ChildInhalerLogs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhaler_logs);
        findViewById(R.id.logsbackbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChildInhalerLogs.this, ChildInhalerMenu.class));
            }
        });
        ArrayList<String[]> rescueLogs = new ArrayList<>();
        ArrayList<String[]> controllerLogs = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            rescueLogs.add(new String[]{
                    "Rescue Log #" + i,
                    "Subtitle 1 for rescue log #" + i,
                    "Subtitle 2 for rescue log #" + i
            });

            controllerLogs.add(new String[]{
                    "Controller Log #" + i,
                    "Subtitle 1 for controller log #" + i,
                    "Subtitle 2 for controller log #" + i
            });
        }

        addLogsToContainer(rescueLogs, findViewById(R.id.linearlayout1));
        addLogsToContainer(controllerLogs, findViewById(R.id.linearlayout2));
    }
    private void addLogsToContainer(ArrayList<String[]> logs, LinearLayout container) {
        for (String[] log : logs) {
            LinearLayout logItem = new LinearLayout(this);
            logItem.setOrientation(LinearLayout.VERTICAL);
            logItem.setPadding(0, 16, 0, 16);
            logItem.setBackgroundColor(Color.argb(40,255,255,255));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 10, 0, 10);
            logItem.setLayoutParams(params);
            TextView title = new TextView(this);
            TextView content = new TextView(this);
            title.setText(log[0]);
            title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            title.setTextColor(0xFF000000);
            logItem.addView(title);
            content.setText(log[1]);
            content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            content.setTextColor(0xFF000000);
            logItem.addView(content);
            container.addView(logItem);
        }
    }
}