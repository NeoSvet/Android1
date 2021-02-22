package ru.neosvet.notes;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.neosvet.notes.exchange.ObserverDate;
import ru.neosvet.notes.exchange.PublisherDate;
import ru.neosvet.notes.note.BaseItem;
import ru.neosvet.notes.note.CurrentBase;

public class NoteFragment extends Fragment implements ObserverDate {
    private static final String ARG_NOTE_ID = "note", ARG_TITLE = "title", ARG_DES = "des";
    private EditText etTitle, etDescription;
    private TextView tvTitle, tvDate, tvDescription;
    private Button btnEditor;
    private static boolean inEdit = false;
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

        if (inEdit) {
            openEditing();
            etTitle.setText(getArguments().getString(ARG_TITLE));
            etDescription.setText(getArguments().getString(ARG_DES));
        }
    }

    public Bundle getMyArguments() {
        Bundle outState = (Bundle)getArguments().clone();
        if (inEdit) {
            outState.putString(ARG_TITLE, etTitle.getText().toString());
            outState.putString(ARG_DES, etDescription.getText().toString());
        }
        return outState;
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
            main.openDate();
        });
        btnEditor.setOnClickListener(v -> {
            if (inEdit) {
                updateNote(etTitle.getText().toString(),
                        etDescription.getText().toString());
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

    private void updateNote(String title, String des) {
        BaseItem note = CurrentBase.get().getNote(noteId);
        if (note == null)
            return;
        note.setTitle(title);
        note.setDescription(des);
        tvTitle.setText(title);
        tvDescription.setText(des);
    }

    private void loadNote() {
        BaseItem note = CurrentBase.get().getNote(noteId);
        if (note == null)
            return;
        tvTitle.setText(note.getTitle());
        tvDate.setText(note.getDateString());
        tvDescription.setText(note.getDescription());
    }

    @Override
    public void updateDate(long date) {
        BaseItem note = CurrentBase.get().getNote(noteId);
        if (note == null)
            return;
        note.setDate(date);
        tvDate.setText(note.getDateString());
    }
}