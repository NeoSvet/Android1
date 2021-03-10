package ru.neosvet.notes.repository;

import androidx.annotation.Nullable;

import java.util.List;

public interface BaseCallbacks {
    void startProgress();

    void onError(String message);

    void putList(@Nullable List<Note> list);

    void deleteNote(String id);

    void putNote(@Nullable Note note);

    void addNote(Note note);
}
