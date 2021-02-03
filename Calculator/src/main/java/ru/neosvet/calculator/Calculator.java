package ru.neosvet.calculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class Calculator {
    enum Actions {
        NONE(""), PLUS("+"), MINUS("–"), MULTIPLICATION("×"), DIVISION("÷");

        private String sym;

        Actions(String s) {
            sym = s;
        }

        @Override
        public String toString() {
            return sym;
        }
    }

    private TextView tvResult;
    private boolean lastIsNumeral = false;
    private Actions action = Actions.NONE;

    public Calculator(TextView textView) {
        tvResult = textView;
    }

    public View.OnClickListener getEqualsEvent() {
        //TODO OnClickListener
        return v -> {
        };
    }

    public View.OnClickListener getPlusEvent() {
        return v -> {
            action = Actions.PLUS;
            appendAction();
        };
    }

    public View.OnClickListener getMinusEvent() {
        return v -> {
            action = Actions.MINUS;
            appendAction();
        };
    }

    public View.OnClickListener getDivisionEvent() {
        return v -> {
            action = Actions.DIVISION;
            appendAction();
        };
    }

    public View.OnClickListener getMultiplicationEvent() {
        return v -> {
            action = Actions.MULTIPLICATION;
            appendAction();
        };
    }

    private void appendAction() {
        if (tvResult.length() > 0)
            tvResult.append(" ");
        tvResult.append(action.toString());
        lastIsNumeral = false;
    }

    public View.OnClickListener getBackspaceEvent() {
      return v -> {
            if (tvResult.length() == 0)
                return;
            String s = tvResult.getText().toString();
            if (lastIsNumeral || s.length() == 1)
                tvResult.setText(s.substring(0, s.length() - 1));
            else
                tvResult.setText(s.substring(0, s.length() - 2));
        };
    }

    public View.OnClickListener getDotEvent() {
        //TODO OnClickListener
        return v -> {
        };
    }

    public View.OnClickListener getNumeralEvent() {
        return numberClickListener;
    }

    private final View.OnClickListener numberClickListener = v -> {
        Button b = (Button) v;
        if (tvResult.length() > 0 && !lastIsNumeral)
            tvResult.append(" ");
        tvResult.append(b.getText().toString());
        lastIsNumeral = true;
    };
}
