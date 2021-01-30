package ru.neosvet.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

public class CalendarActivity extends AppCompatActivity {
    private TextView label;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        label = findViewById(R.id.label);
        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            label.setText("new date: " + day + "." + (month+1) + "." + year);
        });
    }
}