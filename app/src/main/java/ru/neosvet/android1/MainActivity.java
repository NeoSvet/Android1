package ru.neosvet.android1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView label;
    private EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label = findViewById(R.id.label);
        text = findViewById(R.id.text);

        findViewById(R.id.button).setOnClickListener(v -> {
            label.setText(text.getText());
        });

        findViewById(R.id.openEditAct).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EditActivity.class));
        });

        findViewById(R.id.openCalendarAct).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CalendarActivity.class));
        });

        findViewById(R.id.openMyCalculator).setOnClickListener(v -> {
            try {
                startActivity(new Intent("ru.neosvet.calculator"));
            } catch (Exception e) {
                Toast.makeText(this, "This calculator is not installed yet!", Toast.LENGTH_LONG);
            }
        });
    }
}