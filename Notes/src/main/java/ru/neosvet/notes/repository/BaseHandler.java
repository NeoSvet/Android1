package ru.neosvet.notes.repository;

public interface BaseHandler {
    void startProgress();

    void onError(String message);

    void listIsReady();

    void deleteNote(int id);

    void addNote(BaseItem note);

    void updateNote(BaseItem note);
}
