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
    private final String TYPE_MAIN_FRAG = "TYPE_MAIN_FRAG", NOTE_ID = "note", ORIENTATION = "orientation";
    private final byte TYPE_OTHER = 0, TYPE_NOTE = 1, TYPE_DATE = 2;
    private int noteId = -1;
    private boolean isLandOrientation;
    private byte typeMainFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawerMenu(toolbar);
        isLandOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState == null)
            openList();
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
        typeMainFrag = TYPE_OTHER;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        typeMainFrag = savedInstanceState.getByte(TYPE_MAIN_FRAG);
        noteId = savedInstanceState.getInt(NOTE_ID);
        if (noteId > -1)
            checkOrientation(savedInstanceState.getBoolean(ORIENTATION));
    }

    private void checkOrientation(boolean isPrevLand) {
        if (isPrevLand == isLandOrientation)
            return;

        switch (typeMainFrag) {
            case TYPE_NOTE:
                NoteFragment note = getNoteFragment();
                if (note == null) {
                    openNote(noteId);
                    return;
                }
                Bundle args = note.getArguments();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().remove(note).commit();
                manager.executePendingTransactions();
                note.setArguments(args);
                if (isLandOrientation) {
                    openList();
                    manager.beginTransaction()
                            .replace(R.id.note_container, note).commit();
                    typeMainFrag = TYPE_NOTE;
                } else {
                    manager.beginTransaction()
                            .replace(R.id.main_container, note).commit();
                }
                break;
            case TYPE_DATE:
                if (isLandOrientation) {
                    openNote(noteId);
                    typeMainFrag = TYPE_DATE;
                } else {
                    NoteFragment note2 = getNoteFragment();
                    if (note2 != null) //во избежание второго меню заметки, скрываем предыдущее
                        note2.setMenuVisibility(false);
                }
                break;
        }
    }

    private NoteFragment getNoteFragment() {
        for (Fragment f : getSupportFragmentManager().getFragments()) {
            if (f instanceof NoteFragment)
                return (NoteFragment) f;
        }
        return null;
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
        outState.putByte(TYPE_MAIN_FRAG, typeMainFrag);
        outState.putBoolean(ORIENTATION, isLandOrientation);
        outState.putInt(NOTE_ID, noteId);
        super.onSaveInstanceState(outState);
    }

    private void openList() {
        openFragment(ListFragment.newInstance(-1));
    }

    public void openNote(int id) {
        noteId = id;
        NoteFragment note = NoteFragment.newInstance(id);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(isLandOrientation ? R.id.note_container
                        : R.id.main_container, note)
                .commit();
        typeMainFrag = TYPE_NOTE;
    }

    public void openDate() {
        DateFragment date = DateFragment.newInstance(CurrentBase.get().getNote(noteId).getDate());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, date)
                .commit();
        typeMainFrag = TYPE_DATE;
    }

    @Override
    public void onBackPressed() {
        switch (typeMainFrag) {
            case TYPE_OTHER:
                break;
            case TYPE_NOTE:
                NoteFragment note = getNoteFragment();
                if (note != null && note.onBack())
                    return;
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
        CurrentBase.get().getNote(noteId).setDate(date);
    }

    public void removeNote(int id) {
        if (noteId == id)
            noteId = -1;
        NoteFragment note = getNoteFragment();
        getSupportFragmentManager().beginTransaction().remove(note).commit();
        if (isLandOrientation) {
            ListFragment list = null;
            for (Fragment f : getSupportFragmentManager().getFragments()) {
                if (f instanceof ListFragment)
                    list = (ListFragment) f;
            }
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