package ru.neosvet.notes.note;

import java.util.ArrayList;
import java.util.List;

public class SampleBase implements Base {
    private List<Item> notes = new ArrayList<>();

    @Override
    public void open() {
        notes.add(new Item("Note #1", 1613200007650L, "Des #1"));
        notes.add(new Item("Note #2", 1613244000000L, "Des #2"));
        notes.add(new Item("Note #3", 1613244207650L, "Des #3"));
    }

    @Override
    public String[] getListTitles(int offset, int limit) {
        return new String[]{notes.get(0).getTitle(),
                notes.get(1).getTitle(),
                notes.get(2).getTitle()};
    }

    @Override
    public Item getNote(int id) {
        if (id < notes.size())
            return notes.get(id);
        return null;
    }

    @Override
    public void close() {
        notes.clear();
    }
}
