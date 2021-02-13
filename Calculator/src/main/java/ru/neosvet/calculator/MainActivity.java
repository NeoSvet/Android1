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

import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements Calculator.Callback, View.OnClickListener {
    private final String CALCULATOR = "cal";
    private Calculator calculator;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Decorer decorer = new Decorer(this);
        decorer.applyTheme(this);
        setContentView(R.layout.activity_main);
        initToolbar();

        initCalculator();
        initNumeralButtons();
        initOtherButtons();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            buttons[i].setOnClickListener(this);
        }
    }

    private void initOtherButtons() {
        findViewById(R.id.btnPlus).setOnClickListener(v -> calculator.actionPlus());
        findViewById(R.id.btnMinus).setOnClickListener(v -> calculator.actionMinus());
        findViewById(R.id.btnMultiplication).setOnClickListener(v -> calculator.actionMultiplication());
        findViewById(R.id.btnDivision).setOnClickListener(v -> calculator.actionDivision());
        findViewById(R.id.btnDot).setOnClickListener(v -> calculator.printDot());
        View bBackspace = findViewById(R.id.btnBackspace);
        bBackspace.setOnClickListener(v -> calculator.doBackspace());
        bBackspace.setOnLongClickListener(v -> {
            calculator.doClear();
            return true;
        });
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculator.doCalculate());
    }

    @Override
    public void setResult(String value) {
        tvResult.setText(value);
    }

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        calculator.printNumeral(b.getText().toString());
    }
}