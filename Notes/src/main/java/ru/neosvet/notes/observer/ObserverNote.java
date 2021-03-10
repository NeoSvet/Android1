package ru.neosvet.notes.observer;

import ru.neosvet.notes.repository.Note;

public interface ObserverNote {
    void updateNote(Note note);

    void deletedNote(String id);
}
