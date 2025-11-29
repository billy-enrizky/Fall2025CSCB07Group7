package com.example.myapplication.dailycheckin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.example.myapplication.R;
import com.example.myapplication.childmanaging.SignInChildProfileActivity;

import java.util.Calendar;
import java.util.List;

public class FilterCheckInByDate extends AppCompatActivity {

    CalendarView range;
    Button goToSymptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_check_in_history_date);
        this.range = (CalendarView)findViewById(R.id.selectDateRange);
        this.goToSymptoms = (Button)findViewById(R.id.go_to_filter_by_symptoms);
    }

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    public void filterBySymptomsButton(View view) {
        List<Calendar> selectedDates = range.getSelectedDates();
        if (selectedDates.isEmpty()) {
            Toast.makeText(this, "Please select dates", Toast.LENGTH_SHORT).show();
            return;
        }
        Calendar start = selectedDates.getFirst();
        Calendar end = selectedDates.getLast();
        start.getTimeInMillis();
        CheckInHistoryFilters filters = CheckInHistoryFilters.getInstance();
        filters.setStartDate(String.format("%tF", start.getTimeInMillis()));
        filters.setEndDate(String.format("%tF", end.getTimeInMillis()));
        Toast.makeText(this, filters.getStartDate() + " to " + filters.getEndDate(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(FilterCheckInByDate.this, FilterCheckInBySymptoms.class);
        startActivity(intent);
    }

    public void backToChildSignIn (View view) {
        Intent intent = new Intent(FilterCheckInByDate.this, SignInChildProfileActivity.class);
        startActivity(intent);
    }
}
