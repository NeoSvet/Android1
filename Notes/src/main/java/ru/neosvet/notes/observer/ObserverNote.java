package ru.neosvet.notes.observer;

import ru.neosvet.notes.repository.BaseItem;

public interface ObserverNote {
    void updateNote(BaseItem note);

    void deletedNote(int id);
}
