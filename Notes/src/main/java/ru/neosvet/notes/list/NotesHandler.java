package ru.neosvet.notes.list;

public interface NotesHandler {
    void onItemClicked(int id);

    void updateList(int offset);
}