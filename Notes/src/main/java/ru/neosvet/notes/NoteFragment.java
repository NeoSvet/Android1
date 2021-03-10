package ru.neosvet.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import ru.neosvet.notes.observer.ObserverNote;
import ru.neosvet.notes.observer.PublisherNote;
import ru.neosvet.notes.repository.Note;
import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Deleter;

public class NoteFragment extends Fragment implements ObserverNote {
    private static final String ARG_NOTE_ID = "note", ARG_EDIT = "edit",
            ARG_TITLE = "title", ARG_DES = "des";
    private TextInputEditText etTitle, etDescription;
    private MaterialTextView tvTitle, tvDate, tvDescription;
    private MaterialButton btnEditor;
    private boolean inEdit = false;
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
        etTitle.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etTitle.setRawInputType(InputType.TYPE_CLASS_TEXT);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDate = view.findViewById(R.id.tvDate);
        etDescription = view.findViewById(R.id.etDescription);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        btnEditor = view.findViewById(R.id.btnEditor);
        initListeners();

        loadNote();

        Bundle args = getArguments();
        inEdit = args.getBoolean(ARG_EDIT, false);
        if (inEdit) {
            openEditing();
            etTitle.setText(args.getString(ARG_TITLE));
            etDescription.setText(args.getString(ARG_DES));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Bundle args = getArguments();
        args.putBoolean(ARG_EDIT, inEdit);
        if (inEdit) {
            args.putString(ARG_TITLE, etTitle.getText().toString());
            args.putString(ARG_DES, etDescription.getText().toString());
        }
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
            case R.id.delete:
                Deleter deleter = new Deleter(noteId);
                deleter.deleteByDialog(requireContext(), tvTitle.getText().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        PublisherNote.queryChanges(this);
        PublisherNote.subscribe(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        PublisherNote.unsubscribe(this);
    }

    public boolean onBack() {
        if (inEdit) {
            closeEditing();
            return true;
        }
        return false;
    }

    private void initListeners() {
        tvDate.setOnClickListener(v -> {
            MainActivity main = (MainActivity) getActivity();
            main.openDate(noteId, CurrentBase.get().getNote(noteId).getDate());
        });
        btnEditor.setOnClickListener(v -> {
            if (inEdit) {
                Note note = CurrentBase.get().getNote(noteId);
                note.setTitle(etTitle.getText().toString());
                note.setDescription(etDescription.getText().toString());
                CurrentBase.get().pushNote(noteId);
                closeEditing();
            } else {
                etTitle.setText(tvTitle.getText());
                etDescription.setText(tvDescription.getText());
                openEditing();
            }
        });
    }

    private void openEditing() {
        inEdit = true;
        btnEditor.setText(R.string.save);
        tvTitle.setVisibility(View.INVISIBLE);
        etTitle.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.GONE);
        etDescription.setVisibility(View.VISIBLE);
    }

    private void closeEditing() {
        inEdit = false;
        btnEditor.setText(R.string.edit);
        etTitle.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        etDescription.setVisibility(View.GONE);
        tvDescription.setVisibility(View.VISIBLE);
    }

    private void loadNote() {
        Note note = CurrentBase.get().getNote(noteId);
        if (note == null)
            return;
        tvTitle.setText(note.getTitle());
        tvDate.setText(note.getDateString());
        tvDescription.setText(note.getDescription());
    }

    @Override
    public void updateNote(Note note) {
        if (note.getId() != noteId)
            return;
        tvTitle.setText(note.getTitle());
        tvDate.setText(note.getDateString());
        tvDescription.setText(note.getDescription());
    }

    @Override
    public void deletedNote(int id) {
        if (id != noteId)
            return;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getFragmentManager().beginTransaction().remove(this).commit();
        } else {
            closeEditing();
            getActivity().onBackPressed();
        }
    }
}