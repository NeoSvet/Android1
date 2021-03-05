package ru.neosvet.notes.repository;

public interface BaseCallbacks {
    void startProgress();

    void onError(String message);

    void listIsReady();

    void deleteNote(int id);

    void addNote(BaseItem note);

    void updateNote(BaseItem note);
}
