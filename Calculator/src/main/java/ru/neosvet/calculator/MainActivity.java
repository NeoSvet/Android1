package ru.neosvet.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
                .setNumberButtons(getNumberButtons())
                .setDotButton(findViewById(R.id.bDot))
                .setBackspaceButton(findViewById(R.id.bBackspace))
                .setPlusButton(findViewById(R.id.bPlus))
                .setMinusButton(findViewById(R.id.bMunis))
                .setMultiplicationButton(findViewById(R.id.bMultiplication))
                .setDivisionButton(findViewById(R.id.bDivision))
                .setEqualsButton(findViewById(R.id.bEquals));
        try {
            calculator = factory.build();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Button[] getNumberButtons() {
        return new Button[]{findViewById(R.id.bNumber0),
                findViewById(R.id.bNumber1), findViewById(R.id.bNumber2), findViewById(R.id.bNumber3),
                findViewById(R.id.bNumber4), findViewById(R.id.bNumber5), findViewById(R.id.bNumber6),
                findViewById(R.id.bNumber7), findViewById(R.id.bNumber8), findViewById(R.id.bNumber9)
        };
    }

}