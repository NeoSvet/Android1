package ru.neosvet.calculator;

import android.view.View;
import android.widget.Button;

public class Calculator {
    public interface Callback {
        void setResult(String value);
    }

    enum Actions {
        NONE(""), PLUS("+"), MINUS("–"), MULTIPLICATION("×"), DIVISION("÷");

        private final String sym;

        Actions(String s) {
            sym = s;
        }

        @Override
        public String toString() {
            return sym;
        }

        public static Actions parse(String s) {
            if (s.equals(PLUS.toString()))
                return PLUS;
            if (s.equals(MINUS.toString()))
                return MINUS;
            if (s.equals(MULTIPLICATION.toString()))
                return MULTIPLICATION;
            if (s.equals(DIVISION.toString()))
                return DIVISION;
            return NONE;
        }
    }

    private final String DOT = ".";
    private final Callback parent;
    private Actions action = Actions.NONE;
    private String value = "", number1 = "", number2 = "";

    public Calculator(Callback parent) {
        this.parent = parent;
    }

    public void setValue(String value) {
        this.value = value;
        parent.setResult(value);

        number2 = "";
        action = Actions.NONE;

        if (value.length() == 0) {
            number1 = "";
            return;
        }

        String[] m = value.split(" ");

        if (m.length == 5) { //with result
            number1 = m[4];
            return;
        } else
            number1 = m[0];

        if (m.length > 1) //with action
            action = Actions.parse(m[1]);

        if (m.length == 3) //with number2
            number2 = m[2];
    }

    @Override
    public String toString() {
        return value;
    }

    public void doCalculate() {
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
                result = n1 - n2;
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
        value += " = " + result;
        parent.setResult(value);
        action = Actions.NONE;
        number1 = String.valueOf(result);
        number2 = "";
    }

    private void setAction(Actions action) {
        if (value.length() == 0 || number2.length() > 0)
            return;
        this.action = action;
        updateResult();
    }

    private void updateResult() {
        value = (number1 + " " + action.toString() + " " + number2).trim();
        parent.setResult(value);
    }

    public void actionPlus() {
        setAction(Actions.PLUS);
    }

    public void actionMinus() {
        if (value.length() == 0) {
            number1 = "-";
            value = number1;
            parent.setResult(value);
            return;
        }
        setAction(Actions.MINUS);
    }

    public void actionDivision() {
        setAction(Actions.DIVISION);
    }

    public void actionMultiplication() {
        setAction(Actions.MULTIPLICATION);
    }

    public void doBackspace() {
        if (value.length() == 0)
            return;
        if (number2.length() > 0) {
            number2 = number2.substring(0, number2.length() - 1);
        } else if (action == Actions.NONE) {
            number1 = number1.substring(0, number1.length() - 1);
        } else {
            action = Actions.NONE;
        }
        updateResult();
    }

    public void doClear() {
        value = "";
        parent.setResult(value);
        number1 = "";
        number2 = "";
        action = Actions.NONE;
    }

    public void printDot() {
        if (action == Actions.NONE) {
            if (number1.contains(DOT) || number1.length() == 0)
                return;
            number1 += DOT;
        } else {
            if (number2.contains(DOT) || number2.length() == 0)
                return;
            number2 += DOT;
        }
        value += DOT;
        parent.setResult(value);
    }

    public void printNumeral(String numeral) {
        if (action == Actions.NONE)
            number1 += numeral;
        else
            number2 += numeral;
        updateResult();
    }
}
