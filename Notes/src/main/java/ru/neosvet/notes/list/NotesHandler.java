package ru.neosvet.notes.list;

public interface NotesHandler {
    void onItemClicked(int position);

    void updateList(int offset);
}