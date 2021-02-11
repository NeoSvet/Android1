package ru.neosvet.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Calculator.Callback {
    private final String CALCULATOR = "cal";
    private Calculator calculator;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalculator();
        initNumeralButtons();
        initOtherButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(R.string.settings);
        item.setIcon(R.drawable.ic_baseline_settings_32);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SettingsActivity.class));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CALCULATOR, calculator.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator.setValue(savedInstanceState.getString(CALCULATOR));
    }

    private void initCalculator() {
        tvResult = findViewById(R.id.tvResult);
        calculator = new Calculator(this);
    }

    private void initNumeralButtons() {
        Button[] buttons = new Button[]{findViewById(R.id.btnNumeral0),
                findViewById(R.id.btnNumeral1), findViewById(R.id.btnNumeral2), findViewById(R.id.btnNumeral3),
                findViewById(R.id.btnNumeral4), findViewById(R.id.btnNumeral5), findViewById(R.id.btnNumeral6),
                findViewById(R.id.btnNumeral7), findViewById(R.id.btnNumeral8), findViewById(R.id.btnNumeral9)
        };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(calculator.getNumeralClick());
        }
    }

    private void initOtherButtons() {
        findViewById(R.id.btnPlus).setOnClickListener(calculator.getPlusClick());
        findViewById(R.id.btnMinus).setOnClickListener(calculator.getMinusClick());
        findViewById(R.id.btnMultiplication).setOnClickListener(calculator.getMultiplicationClick());
        findViewById(R.id.btnDivision).setOnClickListener(calculator.getDivisionClick());
        findViewById(R.id.btnDot).setOnClickListener(calculator.getDotClick());
        View bBackspace = findViewById(R.id.btnBackspace);
        bBackspace.setOnClickListener(calculator.getBackspaceClick());
        bBackspace.setOnLongClickListener(calculator.getBackspaceLongClick());
        findViewById(R.id.btnEquals).setOnClickListener(calculator.getEqualsClick());
    }

    @Override
    public void setResult(String value) {
        tvResult.setText(value);
    }
}