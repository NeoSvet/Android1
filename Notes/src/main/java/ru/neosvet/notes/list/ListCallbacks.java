package ru.neosvet.notes.list;

public interface ListCallbacks {
    void onItemClicked(String id);

    void onLongItemClicked(String id);
}