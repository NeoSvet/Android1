package ru.neosvet.notes.list;

public interface ListCallbacks {
    void onItemClicked(int id);

    void onLongItemClicked(int id);

    void updateList();
}