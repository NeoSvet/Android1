package ru.neosvet.notes.note;

public interface Base {
    void open();

    BaseItem[] getList(int offset, int limit);

    BaseItem getNote(int id);

    void close();
}
