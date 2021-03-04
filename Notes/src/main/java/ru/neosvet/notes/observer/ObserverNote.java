package ru.neosvet.notes.observer;

public interface ObserverNote {
    void updateDate(int id, long date);

    void updateContent(int id, String title, String description);
}
