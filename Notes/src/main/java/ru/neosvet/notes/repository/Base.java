package ru.neosvet.notes.repository;

public interface Base {
    int LIMIT = 10;

    void open(BaseHandler handler);

    void loadNextPage();

    BaseItem[] getList(int offset);

    BaseItem getNote(int id);

    void deleteNote(int id);

    void pushNote(int id);

    void addNote();

    void close();

    boolean isClosed();
}
