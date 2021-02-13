package ru.neosvet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.neosvet.notes.note.Base;
import ru.neosvet.notes.note.SampleBase;

public class MainActivity extends AppCompatActivity {
    private final String BACK_TO_LIST = "BackToList";
    private boolean isBackToList = false;
    private Base notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBase();

        if (savedInstanceState == null)
            openList();
        else
            isBackToList = savedInstanceState.getBoolean(BACK_TO_LIST);
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
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new ListFragment())
                .commit();
    }

    public void openNote(int id) {
        NoteFragment note = NoteFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, note)
                .commit();
        isBackToList = true;
    }

    @Override
    public void onBackPressed() {
        if (isBackToList) {
            isBackToList = false;
            openList();
            return;
        }
        super.onBackPressed();
    }
}