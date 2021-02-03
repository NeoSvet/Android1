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

    public static class Factory {
        private final Calculator calculator = new Calculator();
        private boolean[] init_state = new boolean[9];

        public Calculator build() throws InstantiationException {
            if (isReady())
                return calculator;
            throw new InstantiationException("Not all views are set-up.");
        }

        private boolean isReady() {
            for (int i = 0; i < init_state.length; i++) {
                if (!init_state[i])
                    return false;
            }
            return true;
        }

        public Factory setTextView(TextView textView) {
            init_state[0] = true;
            calculator.setTextView(textView);
            return this;
        }

        public Factory setNumberButtons(View[] numberButtons) {
            if (numberButtons.length != 10)
                return this;
            init_state[1] = true;
            calculator.setNumberButtons(numberButtons);
            return this;
        }

        public Factory setBackspaceButton(View button) {
            init_state[2] = true;
            calculator.setBackspaceButton(button);
            return this;
        }

        public Factory setDotButton(View button) {
            init_state[3] = true;
            calculator.setDotButton(button);
            return this;
        }

        public Factory setPlusButton(View button) {
            init_state[4] = true;
            calculator.setPlusButton(button);
            return this;
        }

        public Factory setMinusButton(View button) {
            init_state[5] = true;
            calculator.setMinusButton(button);
            return this;
        }

        public Factory setMultiplicationButton(View button) {
            init_state[6] = true;
            calculator.setMultiplicationButton(button);
            return this;
        }

        public Factory setDivisionButton(View button) {
            init_state[7] = true;
            calculator.setDivisionButton(button);
            return this;
        }

        public Factory setEqualsButton(View button) {
            init_state[8] = true;
            calculator.setEqualsButton(button);
            return this;
        }
    } //END FACTORY

    private TextView tvResult;
    private boolean lastIsNumber = false;
    private Actions action = Actions.NONE;

    private Calculator() {
    }

    private void setTextView(TextView textView) {
        tvResult = textView;
    }

    private void setEqualsButton(View button) {
        //TODO setOnClickListener
    }

    private void setPlusButton(View button) {
        button.setOnClickListener( v -> {
            action = Actions.PLUS;
            appendAction();
        });
    }

    private void setMinusButton(View button) {
        button.setOnClickListener( v -> {
            action = Actions.MINUS;
            appendAction();
        });
    }

    private void setDivisionButton(View button) {
        button.setOnClickListener( v -> {
            action = Actions.DIVISION;
            appendAction();
        });
    }

    private void setMultiplicationButton(View button) {
        button.setOnClickListener( v -> {
            action = Actions.MULTIPLICATION;
            appendAction();
        });
    }

    private void appendAction() {
        if (tvResult.length() > 0)
            tvResult.append(" ");
        tvResult.append(action.toString());
        lastIsNumber = false;
    }

    private void setBackspaceButton(View button) {
        button.setOnClickListener(v -> {
            if (tvResult.length() == 0)
                return;
            String s = tvResult.getText().toString();
            if (lastIsNumber || s.length() == 1)
                tvResult.setText(s.substring(0, s.length() - 1));
            else
                tvResult.setText(s.substring(0, s.length() - 2));
        });
    }

    private void setDotButton(View button) {
        //TODO setOnClickListener
    }

    private void setNumberButtons(View[] numberButtons) {
        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i].setOnClickListener(numberClickListener);
        }
    }

    private final View.OnClickListener numberClickListener = v -> {
        Button b = (Button) v;
        if (tvResult.length() > 0 && !lastIsNumber)
            tvResult.append(" ");
        tvResult.append(b.getText().toString());
        lastIsNumber = true;
    };
}
