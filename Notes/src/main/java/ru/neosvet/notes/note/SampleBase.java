package ru.neosvet.notes.note;

import java.util.ArrayList;
import java.util.List;

public class SampleBase implements Base {
    private List<BaseItem> notes = new ArrayList<>();

    @Override
    public void open() {
        notes.add(new BaseItem("Note #1", 1613200007650L, "Des #1"));
        notes.add(new BaseItem("Note #2", 1613244000000L, "Des #2"));
        notes.add(new BaseItem("Note #3", 1613244207650L, "Des #3"));
    }

    @Override
    public BaseItem[] getList(int offset, int limit) {
        if (offset + limit > notes.size())
            limit = notes.size() - offset;
        if (limit < 1)
            return null;
        BaseItem[] m = new BaseItem[limit];
        for (int i = 0; i < limit; i++) {
            m[i] = notes.get(i + offset);
        }
        return m;
    }

    @Override
    public BaseItem getNote(int id) {
        if (id < notes.size())
            return notes.get(id);
        return null;
    }

    @Override
    public boolean removeNote(int id) {
        if (id >= notes.size())
            return false;
        notes.remove(id);
        return true;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
