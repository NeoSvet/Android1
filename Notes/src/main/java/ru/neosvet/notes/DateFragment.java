package ru.neosvet.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Calendar;

import ru.neosvet.notes.exchange.PublisherDate;

public class DateFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private long date;
    private DatePicker dpDate;
    private EditText etHour, etMinute, etFocused;
    private Button btnSave;

    public static DateFragment newInstance(long date) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getLong(ARG_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dpDate = view.findViewById(R.id.dpDate);
        etHour = view.findViewById(R.id.etHour);
        etMinute = view.findViewById(R.id.etMinute);
        btnSave = view.findViewById(R.id.btnSave);
        hideTitleDatePicker();

        showDate();
        initListeners();
    }

    private void initListeners() {
        btnSave.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, dpDate.getYear());
            c.set(Calendar.MONTH, dpDate.getMonth());
            c.set(Calendar.DAY_OF_MONTH, dpDate.getDayOfMonth());
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(etHour.getText().toString()));
            c.set(Calendar.MINUTE, Integer.parseInt(etMinute.getText().toString()));
            PublisherDate.notify(c.getTimeInMillis());
            requireActivity().onBackPressed();
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etFocused == null || etFocused.length() == 0)
                    return;

                try {
                    int n = Integer.parseInt(etFocused.getText().toString());
                    if (n < 0) {
                        etFocused.setText("0");
                        return;
                    }
                    int max;
                    if (etFocused.equals(etHour))
                        max = 23;
                    else
                        max = 59;
                    if (n > max) {
                        etFocused.setText(String.valueOf(max));
                        etFocused.setSelection(2);
                    }
                } catch (Exception e) {
                    etFocused.setText("0");
                }
            }
        };
        etHour.addTextChangedListener(watcher);
        etMinute.addTextChangedListener(watcher);

        View.OnFocusChangeListener onFocus = (v, hasFocus) -> {
            if (hasFocus)
                etFocused = (EditText) v;
            else {
                EditText et = (EditText) v;
                if (et.length() == 0)
                    et.setText("0");
            }
        };
        etHour.setOnFocusChangeListener(onFocus);
        etMinute.setOnFocusChangeListener(onFocus);
    }

    private void hideTitleDatePicker() {
        //в горизонтальной ориентации с ним просто жутко выглядит
        LinearLayout layout = (LinearLayout) dpDate.getChildAt(0);
        layout.getChildAt(0).setVisibility(View.GONE);
    }

    private void showDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        dpDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        etHour.setText(String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
        etMinute.setText(String.valueOf(c.get(Calendar.MINUTE)));
    }
}