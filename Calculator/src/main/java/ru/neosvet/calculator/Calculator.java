package ru.neosvet.calculator;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private final String DOT = ".";
    private TextView tvResult;
    private Actions action = Actions.NONE;
    private String number1 = "", number2 = "";

    public Calculator(TextView textView) {
        tvResult = textView;
    }

    public View.OnClickListener getEqualsClick() {
        return v -> {
            if (number2.length() == 0)
                return;
            float n1 = Float.parseFloat(number1);
            float n2 = Float.parseFloat(number2);
            float result;
            switch (action) {
                case PLUS:
                    result = n1 + n2;
                    break;
                case MINUS:
                    result = n1 + n2;
                    break;
                case MULTIPLICATION:
                    result = n1 * n2;
                    break;
                case DIVISION:
                    result = n1 / n2;
                    break;
                default:
                    return;
            }
            tvResult.setText(number1 + " " + action.toString() + " " + number2 + " = " + result);
        };
    }

    private void setAction(Actions action) {
        if (tvResult.length() == 0 || number2.length() > 0)
            return;
        this.action = action;
        updateResult();
    }

    private void updateResult() {
        tvResult.setText((number1 + " " + action.toString() + " " + number2).trim());
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
            if (number2.length() > 0) {
                number2 = number2.substring(0, number2.length() - 1);
            } else if (action == Actions.NONE) {
                number1 = number1.substring(0, number1.length() - 1);
            } else {
                action = Actions.NONE;
            }
            updateResult();
        };
    }

    public View.OnLongClickListener getBackspaceLongClick() {
        return v -> {
            tvResult.setText("");
            number1 = "";
            number2 = "";
            action = Actions.NONE;
            return true;
        };
    }

    public View.OnClickListener getDotClick() {
        return v -> {
            if (action == Actions.NONE) {
                if (number1.contains(DOT) || number1.length() == 0)
                    return;
                number1 += DOT;
            } else {
                if (number2.contains(DOT) || number2.length() == 0)
                    return;
                number2 += DOT;
            }
            tvResult.append(DOT);
        };
    }

    public View.OnClickListener getNumeralClick() {
        return numeralClickListener;
    }

    private final View.OnClickListener numeralClickListener = v -> {
        Button b = (Button) v;
        String numeral = b.getText().toString();
        if (action == Actions.NONE)
            number1 += numeral;
        else
            number2 += numeral;
        updateResult();
    };
}
