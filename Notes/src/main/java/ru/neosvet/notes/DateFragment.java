package ru.neosvet.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.util.Calendar;

public class DateFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private long date;
    private DatePicker dpDate;

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
        hideTitleDatePicker();

        showDate();
    }

    private void hideTitleDatePicker() {
        //в горизонтальной ориентации с ним просто жутко выглядит
        LinearLayout layout = (LinearLayout)dpDate.getChildAt(0);
        layout.getChildAt(0).setVisibility(View.GONE);
    }

    private void showDate() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        dpDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
    }
}