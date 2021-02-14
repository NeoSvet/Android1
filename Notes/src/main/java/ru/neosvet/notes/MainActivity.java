package ru.neosvet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import ru.neosvet.notes.note.Base;
import ru.neosvet.notes.note.SampleBase;

public class MainActivity extends AppCompatActivity {
    private final String TYPE_MAIN_FRAG = "TYPE_MAIN_FRAG", ID_NOTE = "note", ORIENTATION = "orientation";
    private final byte TYPE_LIST = 0, TYPE_NOTE = 1, TYPE_DATE = 2;
    private int id_note = -1;
    private boolean isLandOrientation;
    private Base notes;
    private byte typeMainFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLandOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        initBase();

        if (savedInstanceState == null)
            openList();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        typeMainFrag = savedInstanceState.getByte(TYPE_MAIN_FRAG);
        id_note = savedInstanceState.getInt(ID_NOTE);
        checkOrientation(savedInstanceState.getBoolean(ORIENTATION));
    }

    private void checkOrientation(boolean isPrevLand) {
        if (isPrevLand == isLandOrientation)
            return;

        switch (typeMainFrag) {
            case TYPE_LIST:
                break;
            case TYPE_NOTE:
                if (isLandOrientation)
                    openList();
                openNote(id_note);
                break;
            case TYPE_DATE:
                if (isLandOrientation)
                    openNote(id_note);
                break;
        }
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
        outState.putByte(TYPE_MAIN_FRAG, typeMainFrag);
        outState.putBoolean(ORIENTATION, isLandOrientation);
        outState.putInt(ID_NOTE, id_note);
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new ListFragment())
                .commit();
        typeMainFrag = TYPE_LIST;
    }

    public void openNote(int id) {
        id_note = id;
        NoteFragment note = NoteFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(isLandOrientation ? R.id.note_container
                        : R.id.main_container, note)
                .commit();
        if (!isLandOrientation)
            typeMainFrag = TYPE_NOTE;
    }

    public void openDate() {
        DateFragment date = DateFragment.newInstance(notes.getNote(id_note).getDate());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, date)
                .commit();
        typeMainFrag = TYPE_DATE;
    }

    @Override
    public void onBackPressed() {
        switch (typeMainFrag) {
            case TYPE_LIST:
                break;
            case TYPE_NOTE:
                if (isLandOrientation)
                    break;
                else {
                    id_note = -1;
                    openList();
                    return;
                }
            case TYPE_DATE:
                if (isLandOrientation)
                    openList();
                else
                    openNote(id_note);
                return;
        }
        super.onBackPressed();
    }
}