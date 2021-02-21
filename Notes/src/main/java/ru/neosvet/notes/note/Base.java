package ru.neosvet.notes.note;

public interface Base {
    void open();

    String[] getListTitles(int offset, int limit);

    BaseItem getNote(int id);

    void close();
}
