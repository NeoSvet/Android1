package ru.neosvet.notes.observer;

import java.util.List;

import ru.neosvet.notes.repository.Note;

public interface ObserverList {
    void putList(List<Note> list);

    void addItem(Note note);
}
