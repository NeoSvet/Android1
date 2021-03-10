package ru.neosvet.notes;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Note;

public class DateFragment extends Fragment {
    public static final String ARG_TIME = "time", ARG_ID = "id";
    private int noteId;
    private DatePicker dpDate;
    private TextInputEditText etHour, etMinute, etFocused;
    private MaterialButton btnSave;
    private Calendar calendar = Calendar.getInstance();

    public static DateFragment newInstance(int id, long time) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putLong(ARG_TIME, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_date, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem item = menu.add(R.string.now);
        item.setIcon(R.drawable.ic_baseline_today_24);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showDate(Calendar.getInstance());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dpDate = view.findViewById(R.id.dpDate);
        etHour = view.findViewById(R.id.etHour);
        etMinute = view.findViewById(R.id.etMinute);
        btnSave = view.findViewById(R.id.btnSave);
        hideTitleDatePicker();

        if (getArguments() != null) {
            noteId = getArguments().getInt(ARG_ID);
            calendar.setTimeInMillis(getArguments().getLong(ARG_TIME));
        }
        showDate(calendar);
        initListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        getArguments().putLong(ARG_TIME, getCurrentDate());
    }

    private void initListeners() {
        btnSave.setOnClickListener(v -> {
            Note note = CurrentBase.get().getNote(noteId);
            note.setDate(getCurrentDate());
            CurrentBase.get().pushNote(note);
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
                etFocused = (TextInputEditText) v;
            else {
                TextInputEditText et = (TextInputEditText) v;
                if (et.length() == 0)
                    et.setText("0");
            }
        };
        etHour.setOnFocusChangeListener(onFocus);
        etMinute.setOnFocusChangeListener(onFocus);
    }

    private long getCurrentDate() {
        calendar.set(Calendar.YEAR, dpDate.getYear());
        calendar.set(Calendar.MONTH, dpDate.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, dpDate.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(etHour.getText().toString()));
        calendar.set(Calendar.MINUTE, Integer.parseInt(etMinute.getText().toString()));
        return calendar.getTimeInMillis();
    }

    private void hideTitleDatePicker() {
        //в горизонтальной ориентации с ним просто жутко выглядит
        LinearLayout layout = (LinearLayout) dpDate.getChildAt(0);
        layout.getChildAt(0).setVisibility(View.GONE);
    }

    private void showDate(Calendar calendar) {
        dpDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        etHour.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        etMinute.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
    }
}