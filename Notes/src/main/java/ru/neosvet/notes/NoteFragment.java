package ru.neosvet.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ru.neosvet.notes.note.Item;

public class NoteFragment extends Fragment {
    private static final String ARG_NOTE_ID = "note";
    private EditText etTitle, etDes;
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
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTitle = view.findViewById(R.id.etTitle);
        tvDate = view.findViewById(R.id.tvDate);
        etDes = view.findViewById(R.id.etDes);
        initListeners();

        loadNote();
    }

    private void initListeners() {
        tvDate.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            main.openDate();
        });
    }

    private void loadNote() {
        MainActivity main = (MainActivity) getActivity();
        Item note = main.getNotes().getNote(noteId);
        etTitle.setText(note.getTitle());
        tvDate.setText(note.getDateString());
        etDes.setText(note.getDes());
    }
}