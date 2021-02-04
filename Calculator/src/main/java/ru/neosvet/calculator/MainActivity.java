package ru.neosvet.calculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private final String CALCULATOR = "cal";
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalculator();
        initNumeralButtons();
        initOtherButtons();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(CALCULATOR, calculator.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator.parseValue(savedInstanceState.getString(CALCULATOR));
    }

    private void initCalculator() {
        calculator = new Calculator(findViewById(R.id.tvResult));
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

}