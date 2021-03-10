package ru.neosvet.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Note;

public class RenameFragment extends BottomSheetDialogFragment {
    private static final String ARG_NOTE_ID = "note";
    private Note note;
    private TextInputEditText etNoteTitle;

    public static RenameFragment create(int noteId) {
        RenameFragment fragment = new RenameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int id = getArguments().getInt(ARG_NOTE_ID);
            note = CurrentBase.get().getNote(id);
        } else
            note = new Note();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rename, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNoteTitle = view.findViewById(R.id.etNoteTitle);
        etNoteTitle.setText(note.getTitle());
        View btnRename = view.findViewById(R.id.btnRename);
        btnRename.setOnClickListener(v -> {
            note.setTitle(etNoteTitle.getText().toString());
            CurrentBase.get().pushNote(note);
            dismiss();
        });
    }
}