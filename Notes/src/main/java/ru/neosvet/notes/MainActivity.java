package ru.neosvet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import ru.neosvet.notes.note.Base;
import ru.neosvet.notes.note.SampleBase;

public class MainActivity extends AppCompatActivity {
    private final String BACK_TO_LIST = "BackToList", ID_NOTE = "note";
    private int id_note = -1;
    private boolean isBackToList = false;
    private boolean isLandscapeOrientation;
    private Base notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        initBase();

        if (savedInstanceState == null)
            openList();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isBackToList = savedInstanceState.getBoolean(BACK_TO_LIST);
        id_note = savedInstanceState.getInt(ID_NOTE);
        checkOrientation();
    }

    private void checkOrientation() {
        if (!isLandscapeOrientation) {
            if (id_note > -1 )
                openNote(id_note);
            return;
        }

        if (!isBackToList)
            return;

        openList();
        if (id_note > -1 )
            openNote(id_note);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        notes.open();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notes.close();
    }

    private void initBase() {
        notes = new SampleBase();
        notes.open();
    }

    public Base getNotes() {
        return notes;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(BACK_TO_LIST, isBackToList);
        outState.putInt(ID_NOTE, id_note);
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new ListFragment())
                .commit();
    }

    public void openNote(int id) {
        id_note = id;
        NoteFragment note = NoteFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(isLandscapeOrientation ? R.id.note_container
                        : R.id.main_container, note)
                .commit();
        isBackToList = !isLandscapeOrientation;
    }

    @Override
    public void onBackPressed() {
        if (isBackToList) {
            id_note = -1;
            isBackToList = false;
            openList();
            return;
        }
        super.onBackPressed();
    }
}