package ru.neosvet.notes.note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RandomBase implements Base {
    private List<BaseItem> notes = new ArrayList<>();

    @Override
    public void open() {
    }

    @Override
    public BaseItem[] getList(int offset, int limit) {
        if (offset + limit > notes.size())
            generateTo(offset + limit);
        BaseItem[] m = new BaseItem[limit];
        for (int i = 0; i < limit; i++) {
            m[i] = notes.get(i + offset);
        }
        return m;
    }

    private void generateTo(int size) {
        Random r = new Random();
        Calendar date;
        int n;
        for (int i = notes.size(); i < size; i++) {
            date = Calendar.getInstance();
            n = r.nextInt(date.get(Calendar.YEAR) - 2015) + 2015;
            date.set(Calendar.YEAR, n);
            n = r.nextInt(date.getActualMaximum(Calendar.DAY_OF_YEAR) - 1) + 1;
            date.set(Calendar.DAY_OF_YEAR, n);
            n = r.nextInt(1440);
            date.set(Calendar.MINUTE, n);
            notes.add(new BaseItem("Note #" + i, date.getTimeInMillis(), "Des #" + i));
        }
    }

    @Override
    public BaseItem getNote(int id) {
        if (id >= notes.size())
            generateTo(id + 1);
        return notes.get(id);
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
