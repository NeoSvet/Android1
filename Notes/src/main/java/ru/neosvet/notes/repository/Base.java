package ru.neosvet.notes.repository;

public interface Base {
    void open();

    BaseItem[] getList(int offset, int limit);

    BaseItem getNote(int id);

    boolean removeNote(int id);

    BaseItem addNote();

    void close();

    boolean isClosed();
}
