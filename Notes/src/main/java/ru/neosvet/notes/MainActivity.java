package ru.neosvet.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import ru.neosvet.notes.observer.PublisherList;
import ru.neosvet.notes.observer.PublisherNote;
import ru.neosvet.notes.repository.BaseCallbacks;
import ru.neosvet.notes.repository.CurrentBase;
import ru.neosvet.notes.repository.Note;

public class MainActivity extends AppCompatActivity implements BaseCallbacks {
    private final String MAIN_STACK = "stack", ORIENTATION = "orientation",
            TAG_LIST = "list", TAG_NOTE = "note", TAG_DATE = "date";
    private boolean isLandOrientation;
    private FragmentManager manager;
    private MaterialTextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawerMenu(toolbar);
        isLandOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        manager = getSupportFragmentManager();
        tvStatus = findViewById(R.id.tvStatus);
        CurrentBase.init(this);

        if (savedInstanceState == null) {
            //пришлось повторить код, ибо если использовать openList(),
            //то будет добавлен в стэк и при возрате назад будет пустой экран
            manager.beginTransaction()
                    .replace(R.id.main_container, new ListFragment(), TAG_LIST)
                    .commit();
        }
    }

    private void initDrawerMenu(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (navigateFragment(id)) {
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
                return false;
            }
        });
    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.nav_notes:
                openList();
                return true;
            case R.id.nav_settings:
                openFragment(new SettingsFragment());
                return true;
            case R.id.nav_help:
                openFragment(new HelpFragment());
                return true;
        }
        return false;
    }

    private void openFragment(Fragment fragment) {
        manager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(MAIN_STACK)
                .commit();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        checkOrientation(savedInstanceState.getBoolean(ORIENTATION));
    }

    private void checkOrientation(boolean isPrevLand) {
        if (isPrevLand == isLandOrientation)
            return;

        Fragment note = manager.findFragmentByTag(TAG_NOTE);
        if (note == null) {
            return;
        }
        Bundle args = note.getArguments();
        if (!isLandOrientation) //prev orientation is land
            manager.beginTransaction().remove(note).commit();
        //note.setMenuVisibility(false); //скрываем меню заметки открытой во второй области
        note = new NoteFragment();
        note.setArguments(args);
        long time = 0;
        String id = null;
        Fragment date = manager.findFragmentByTag(TAG_DATE);
        if (date != null) {
            id = date.getArguments().getString(DateFragment.ARG_ID);
            time = date.getArguments().getLong(DateFragment.ARG_TIME);
            super.onBackPressed(); //close date
        }
        if (isLandOrientation) { //prev orientation is port
            super.onBackPressed(); //close note
            manager.beginTransaction()
                    .replace(R.id.note_container, note, TAG_NOTE)
                    .commit();
        } else { //prev orientation is land
            manager.beginTransaction()
                    .replace(R.id.main_container, note, TAG_NOTE)
                    .addToBackStack(MAIN_STACK)
                    .commit();
        }
        if (id != null)
            openDate(id, time);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CurrentBase.get().close();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ORIENTATION, isLandOrientation);
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        manager.beginTransaction()
                .replace(R.id.main_container, new ListFragment(), TAG_LIST)
                .addToBackStack(MAIN_STACK)
                .commit();
    }

    public void openNote(String id) {
        NoteFragment note = NoteFragment.newInstance(id);
        if (isLandOrientation) {
            manager.beginTransaction()
                    .replace(R.id.note_container, note, TAG_NOTE)
                    .commit();
        } else {
            manager.beginTransaction()
                    .replace(R.id.main_container, note, TAG_NOTE)
                    .addToBackStack(MAIN_STACK)
                    .commit();
        }
    }

    public void openDate(String noteId, long time) {
        manager.beginTransaction()
                .replace(R.id.main_container, DateFragment.newInstance(noteId, time), TAG_DATE)
                .addToBackStack(MAIN_STACK)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (manager.findFragmentByTag(TAG_DATE) == null) {
            NoteFragment note = (NoteFragment) manager.findFragmentByTag(TAG_NOTE);
            if (note != null && note.onBack())
                return;
        }
        super.onBackPressed();
    }

    @Override
    public void startProgress() {
        tvStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String message) {
        tvStatus.setVisibility(View.GONE);
        Toast.makeText(this, getResources().getString(R.string.error)
                + ": " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void putList(@Nullable List<Note> list) {
        tvStatus.setVisibility(View.GONE);
        if (list != null)
            PublisherList.notify(list);
    }

    @Override
    public void deleteNote(String id) {
        tvStatus.setVisibility(View.GONE);
        PublisherNote.deleted(id);
    }

    @Override
    public void putNote(@Nullable Note note) {
        tvStatus.setVisibility(View.GONE);
        if (note != null)
            PublisherNote.notify(note);
    }

    @Override
    public void addNote(Note note) {
        tvStatus.setVisibility(View.GONE);
        PublisherList.addItem(note);
        openNote(note.getId());
    }
}