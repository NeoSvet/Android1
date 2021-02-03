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

    public View.OnClickListener getEqualsClick() {
        //TODO OnClickListener
        return v -> {
        };
    }

    private void setAction(Actions action) {
        if (tvResult.length() == 0)
            return;
        if (lastIsNumeral) {
            tvResult.append(" ");
        } else {
            String s = tvResult.getText().toString();
            tvResult.setText(s.substring(0, s.length() - 1));
        }
        this.action = action;
        tvResult.append(action.toString());
        lastIsNumeral = false;
    }

    public View.OnClickListener getPlusClick() {
        return v -> {
            setAction(Actions.PLUS);
        };
    }

    public View.OnClickListener getMinusClick() {
        return v -> {
            setAction(Actions.MINUS);
        };
    }

    public View.OnClickListener getDivisionClick() {
        return v -> {
            setAction(Actions.DIVISION);
        };
    }

    public View.OnClickListener getMultiplicationClick() {
        return v -> {
            setAction(Actions.MULTIPLICATION);
        };
    }

    public View.OnClickListener getBackspaceClick() {
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

    public View.OnClickListener getDotClick() {
        //TODO OnClickListener
        return v -> {
        };
    }

    public View.OnClickListener getNumeralClick() {
        return numeralClickListener;
    }

    private final View.OnClickListener numeralClickListener = v -> {
        Button b = (Button) v;
        if (tvResult.length() > 0 && !lastIsNumeral)
            tvResult.append(" ");
        tvResult.append(b.getText().toString());
        lastIsNumeral = true;
    };
}
