package ru.neosvet.notes.repository;

public interface Base {
    void open(BaseCallbacks callbacks);

    void requestList();

    void requestNote(String id);

    void deleteNote(String id);

    void pushNote(Note note);

    void addNote();

    void close();

    boolean isClosed();
}
