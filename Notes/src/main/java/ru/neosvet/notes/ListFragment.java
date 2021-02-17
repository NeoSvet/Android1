package ru.neosvet.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListTo((ViewGroup) view);
    }

    private void loadListTo(ViewGroup container) {
        float size = getResources().getDimension(R.dimen.text_size);
        int padding = getResources().getDimensionPixelSize(R.dimen.default_margin);
        String[] titles = getTitles();

        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(container.getContext());
            textView.setText(titles[i]);
            textView.setId(i);
            textView.setPadding(padding, padding, 0, 0);
            textView.setTextSize(size);
            textView.setOnClickListener(this);
            container.addView(textView);
        }
    }

    private String[] getTitles() {
        MainActivity main = (MainActivity) getActivity();
        return main.getNotes().getListTitles(0, 10);
    }

    @Override
    public void onClick(View v) {
        MainActivity main = (MainActivity) getActivity();
        main.openNote(v.getId());
    }
}