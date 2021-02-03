package ru.neosvet.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalculator();
        initNumeralButtons();
        initOtherButtons();
    }

    private void initCalculator() {
        calculator = new Calculator(findViewById(R.id.tvResult));
    }

    private void initNumeralButtons() {
        Button[] buttons = new Button[]{findViewById(R.id.bNumeral0),
                findViewById(R.id.bNumeral1), findViewById(R.id.bNumeral2), findViewById(R.id.bNumeral3),
                findViewById(R.id.bNumeral4), findViewById(R.id.bNumeral5), findViewById(R.id.bNumeral6),
                findViewById(R.id.bNumeral7), findViewById(R.id.bNumeral8), findViewById(R.id.bNumeral9)
        };
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setOnClickListener(calculator.getNumeralClick());
        }
    }

    private void initOtherButtons() {
        findViewById(R.id.bPlus).setOnClickListener(calculator.getPlusClick());
        findViewById(R.id.bMinus).setOnClickListener(calculator.getMinusClick());
        findViewById(R.id.bMultiplication).setOnClickListener(calculator.getMultiplicationClick());
        findViewById(R.id.bDivision).setOnClickListener(calculator.getDivisionClick());
        findViewById(R.id.bDot).setOnClickListener(calculator.getDotClick());
        findViewById(R.id.bBackspace).setOnClickListener(calculator.getBackspaceClick());
        findViewById(R.id.bEquals).setOnClickListener(calculator.getEqualsClick());
    }

}