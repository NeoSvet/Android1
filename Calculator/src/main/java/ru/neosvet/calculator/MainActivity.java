package ru.neosvet.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Calculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalculator();
    }

    private void initCalculator() {
        Calculator.Factory factory = new Calculator.Factory()
                .setTextView(findViewById(R.id.tvResult))
                .setNumeralButtons(getNumeralButtons())
                .setDotButton(findViewById(R.id.bDot))
                .setBackspaceButton(findViewById(R.id.bBackspace))
                .setPlusButton(findViewById(R.id.bPlus))
                .setMinusButton(findViewById(R.id.bMinus))
                .setMultiplicationButton(findViewById(R.id.bMultiplication))
                .setDivisionButton(findViewById(R.id.bDivision))
                .setEqualsButton(findViewById(R.id.bEquals));
        try {
            calculator = factory.build();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Button[] getNumeralButtons() {
        return new Button[]{findViewById(R.id.bNumeral0),
                findViewById(R.id.bNumeral1), findViewById(R.id.bNumeral2), findViewById(R.id.bNumeral3),
                findViewById(R.id.bNumeral4), findViewById(R.id.bNumeral5), findViewById(R.id.bNumeral6),
                findViewById(R.id.bNumeral7), findViewById(R.id.bNumeral8), findViewById(R.id.bNumeral9)
        };
    }

}