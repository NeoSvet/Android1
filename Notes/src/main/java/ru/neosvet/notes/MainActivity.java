package ru.neosvet.notes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import ru.neosvet.notes.exchange.ObserverDate;
import ru.neosvet.notes.exchange.PublisherDate;
import ru.neosvet.notes.note.CurrentBase;

public class MainActivity extends AppCompatActivity implements ObserverDate {
    private final String MAIN_STACK = "stack", NOTE_ID = "note", ORIENTATION = "orientation",
            TAG_LIST = "list", TAG_NOTE = "note", TAG_DATE = "date";
    private int noteId = -1;
    private boolean isLandOrientation;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawerMenu(toolbar);
        isLandOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        manager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            //пришлось повторить код, ибо если использовать openList(),
            //то будет добавлен в стэк и при возрате назад будет пустой экран
            manager.beginTransaction()
                    .replace(R.id.main_container, ListFragment.newInstance(-1), TAG_LIST)
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
        noteId = savedInstanceState.getInt(NOTE_ID);
        if (noteId > -1)
            checkOrientation(savedInstanceState.getBoolean(ORIENTATION));
    }

    private void checkOrientation(boolean isPrevLand) {
        if (isPrevLand == isLandOrientation)
            return;

        if (noteId == -1)
            return;

        Fragment note = manager.findFragmentByTag(TAG_NOTE);
        if (note == null) {
            openNote(noteId);
            return;
        }
        Bundle args = note.getArguments();
        if (!isLandOrientation) //prev orientation is land
            manager.beginTransaction().remove(note).commit();
        //note.setMenuVisibility(false); //скрываем меню заметки открытой во второй области
        note = new NoteFragment();
        note.setArguments(args);
        long time = 0;
        Fragment date = manager.findFragmentByTag(TAG_DATE);
        if (date != null) {
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
        if (time > 0)
            openDate(time);
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
        CurrentBase.get().close();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ORIENTATION, isLandOrientation);
        outState.putInt(NOTE_ID, noteId);
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        manager.beginTransaction()
                .replace(R.id.main_container, ListFragment.newInstance(-1), TAG_LIST)
                .addToBackStack(MAIN_STACK)
                .commit();
    }

    public void openNote(int id) {
        noteId = id;
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

    public void openDate(long time) {
        manager.beginTransaction()
                .replace(R.id.main_container, DateFragment.newInstance(time), TAG_DATE)
                .addToBackStack(MAIN_STACK)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (noteId > -1 && manager.findFragmentByTag(TAG_DATE) == null) {
            NoteFragment note = (NoteFragment) manager.findFragmentByTag(TAG_NOTE);
            if (note != null && note.onBack())
                return;
            noteId = -1;
        }
        super.onBackPressed();
    }

    @Override
    public void updateDate(long date) {
        CurrentBase.get().getNote(noteId).setDate(date);
    }

    public void removeNoteFragment(int id) {
        if (!isLandOrientation || id != noteId)
            return;
        noteId = -1;
        Fragment note = manager.findFragmentByTag(TAG_NOTE);
        manager.beginTransaction().remove(note).commit();
    }

    public void removeNote(int id) {
        if (noteId == id)
            noteId = -1;
        Fragment note = manager.findFragmentByTag(TAG_NOTE);
        manager.beginTransaction().remove(note).commit();
        if (isLandOrientation) {
            ListFragment list = (ListFragment) manager.findFragmentByTag(TAG_LIST);
            if (list != null) {
                int pos = list.findPosById(id);
                if (pos == -1)
                    return;
                list.removeItem(pos);
                return;
            }
        }
        openFragment(ListFragment.newInstance(id));
    }
}