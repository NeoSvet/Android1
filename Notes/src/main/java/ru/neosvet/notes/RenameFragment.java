package ru.neosvet.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import ru.neosvet.notes.observer.ObserverNote;
import ru.neosvet.notes.observer.PublisherNote;
import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Note;

public class RenameFragment extends BottomSheetDialogFragment implements ObserverNote {
    private static final String ARG_NOTE_ID = "note";
    private Note note;
    private String noteId;
    private TextInputEditText etNoteTitle;

    public static RenameFragment create(String noteId) {
        RenameFragment fragment = new RenameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            noteId = getArguments().getString(ARG_NOTE_ID);
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
        View btnRename = view.findViewById(R.id.btnRename);
        btnRename.setOnClickListener(v -> {
            if (note == null)
                return;
            note.setTitle(etNoteTitle.getText().toString());
            CurrentBase.get().pushNote(note);
            dismiss();
        });

        loadNote();
    }

    @Override
    public void onStop() {
        super.onStop();
        PublisherNote.unsubscribe(this);
    }

    private void loadNote() {
        PublisherNote.subscribe(this);
        CurrentBase.get().requestNote(noteId);
    }

    @Override
    public void updateNote(Note note) {
        if (!noteId.equals(note.getId()))
            return;
        this.note = note;
        etNoteTitle.setText(note.getTitle());
    }

    @Override
    public void deletedNote(String id) {
        if (noteId.equals(id))
            dismiss();
    }
}