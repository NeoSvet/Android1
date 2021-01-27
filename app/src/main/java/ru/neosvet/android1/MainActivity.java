package ru.neosvet.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView label;
    private EditText text;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.label);
        text = findViewById(R.id.text);
        calendarView = findViewById(R.id.calendarView);

        findViewById(R.id.button).setOnClickListener(v -> {
            label.setText(text.getText());
        });

        findViewById(R.id.openEditAct).setOnClickListener(v -> {
           startActivity(new Intent(MainActivity.this, EditActivity.class));
        });

        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            label.setText("new date: " + day + "." + (month+1) + "." + year);
        });
    }
}