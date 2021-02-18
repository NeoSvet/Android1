package ru.neosvet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import ru.neosvet.notes.exchange.ObserverDate;
import ru.neosvet.notes.exchange.PublisherDate;
import ru.neosvet.notes.note.Base;
import ru.neosvet.notes.note.SampleBase;

public class MainActivity extends AppCompatActivity implements ObserverDate {
    private final String TYPE_MAIN_FRAG = "TYPE_MAIN_FRAG", NOTE_ID = "note", ORIENTATION = "orientation";
    private final byte TYPE_LIST = 0, TYPE_NOTE = 1, TYPE_DATE = 2;
    private int noteId = -1;
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
        noteId = savedInstanceState.getInt(NOTE_ID);
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
                openNote(noteId);
                break;
            case TYPE_DATE:
                if (isLandOrientation)
                    openNote(noteId);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        notes.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isLandOrientation)
            PublisherDate.subscribe(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PublisherDate.unsubscribe();
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
        outState.putInt(NOTE_ID, noteId);
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
        noteId = id;
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
        DateFragment date = DateFragment.newInstance(notes.getNote(noteId).getDate());
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
                    noteId = -1;
                    openList();
                    return;
                }
            case TYPE_DATE:
                if (isLandOrientation)
                    openList();
                else
                    openNote(noteId);
                return;
        }
        super.onBackPressed();
    }

    @Override
    public void updateDate(long date) {
        notes.getNote(noteId).setDate(date);
    }
}