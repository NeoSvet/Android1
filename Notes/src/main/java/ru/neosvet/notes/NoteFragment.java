package ru.neosvet.notes;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.neosvet.notes.exchange.ObserverDate;
import ru.neosvet.notes.exchange.PublisherDate;
import ru.neosvet.notes.note.BaseItem;

public class NoteFragment extends Fragment implements ObserverDate {
    private static final String ARG_NOTE_ID = "note";
    private EditText etTitle, etDescription;
    private TextView tvDate;
    private int noteId;

    public static NoteFragment newInstance(int noteId) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteId = getArguments().getInt(ARG_NOTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTitle = view.findViewById(R.id.etTitle);
        tvDate = view.findViewById(R.id.tvDate);
        etDescription = view.findViewById(R.id.etDescription);
        initListeners();

        loadNote();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(requireContext(), R.string.share, Toast.LENGTH_SHORT).show();
                break;
            case R.id.attach:
                Toast.makeText(requireContext(), R.string.attach, Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            PublisherDate.subscribe(this);
    }

    private void initListeners() {
        tvDate.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            main.openDate();
        });
    }

    private void loadNote() {
        MainActivity main = (MainActivity) getActivity();
        BaseItem note = main.getNotes().getNote(noteId);
        if (note == null)
            return;
        etTitle.setText(note.getTitle());
        tvDate.setText(note.getDateString());
        etDescription.setText(note.getDescription());
    }

    @Override
    public void updateDate(long date) {
        MainActivity main = (MainActivity) getActivity();
        BaseItem note = main.getNotes().getNote(noteId);
        if (note == null)
            return;
        note.setDate(date);
        tvDate.setText(note.getDateString());
    }
}