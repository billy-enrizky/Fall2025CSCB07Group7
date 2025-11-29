package com.example.myapplication.dailycheckin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.myapplication.R;
import com.example.myapplication.ResultCallBack;
import com.example.myapplication.UserManager;
import com.example.myapplication.childmanaging.SignInChildProfileActivity;
import com.example.myapplication.userdata.AccountType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ViewCheckInHistory extends AppCompatActivity {
    TextView historyTextTitle;
    TextView historyText;
    String history = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_history);
        this.historyTextTitle = (TextView)findViewById(R.id.view_history_title);
        historyTextTitle.setText("History for " + SignInChildProfileActivity.getCurrentChild().getName());
        this.historyText = (TextView)findViewById(R.id.display_history);
        createHistory();

    }

    public void returnToSymptoms(View view) {
        Intent intent = new Intent(this, FilterCheckInBySymptoms.class);
        startActivity(intent);
    }

    public void createHistory() {
        CheckInHistoryFilters filters = CheckInHistoryFilters.getInstance();
        CheckInModel.readFromDB(CheckInHistoryFilters.getInstance().getUsername(), filters.getStartDate(), filters.getEndDate(), new ResultCallBack<HashMap<String,DailyCheckin>>(){
            @Override
            public void onComplete(HashMap<String,DailyCheckin> result){
                ArrayList<String>datesInOrder = new ArrayList<>(result.keySet());
                //Toast.makeText(ViewCheckInHistory.this, "# of dates: " + datesInOrder.size(), Toast.LENGTH_SHORT).show();
                Collections.sort(datesInOrder);
                int datesProcessed = 0;
                for (String date: datesInOrder) {
                    DailyCheckin entry = result.get(date);
                    if (entry != null && filters.matchFilters(entry)) {
                        datesProcessed++;
                        String message = "" + date + "\n";
                        message = message + "Night waking: " + entry.getNightWaking() + "\n";
                        message = message + entry.getActivityLimits() + "\n";
                        message = message + "Cough/Wheeze level: " + entry.getCoughWheezeLevel() + "\n";
                        if (!UserManager.currentUser.getAccount().equals(AccountType.PROVIDER) || SignInChildProfileActivity.getCurrentChild().getPermission().getTriggers()) {
                            message = message + "Triggers: ";
                            for (String trigger : entry.getTriggers()) {
                                message = message + trigger + ", ";
                            }
                        }
                        message = message + "\n";
                        history = history + message + "\n";
                    }
                }
                //Toast.makeText(ViewCheckInHistory.this, "# of entries: " + datesProcessed, Toast.LENGTH_SHORT).show();
                if (history.isEmpty()) {
                    history = "No data for selected filters.";
                }
                historyText.setText(history);
                historyText.setTextSize(14);
            }
        });

    }
}
