package ru.neosvet.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private final String BACK_TO_LIST = "BackToList";
    private boolean isBackToList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            openList();
        else
            isBackToList = savedInstanceState.getBoolean(BACK_TO_LIST);
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