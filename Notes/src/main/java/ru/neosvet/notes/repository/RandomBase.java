package ru.neosvet.notes.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RandomBase implements Base {
    private int last_id = -1;
    private List<BaseItem> notes = new ArrayList<>();
    private BaseHandler handler;

    @Override
    public void open(BaseHandler handler) {
        this.handler = handler;
    }

    @Override
    public void loadNextPage() {
    }

    @Override
    public BaseItem[] getList(int offset) {
        int limit = LIMIT;
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
            last_id++;
            notes.add(new BaseItem(last_id, "Note #" + i + ", id " + last_id,
                    date.getTimeInMillis(), "Des #" + i));
        }
    }

    @Override
    public BaseItem getNote(int id) {
        int index = findIndexById(id);
        if (index == -1)
            return null;
        return notes.get(index);
    }

    private int findIndexById(int id) {
        int n;
        if (id >= notes.size()) {
            n = notes.size() - 1;
            while (n > -1 && notes.get(n).getId() != id)
                n--;
            return n;
        }
        n = notes.get(id).getId();
        if (n < id) {
            n = id + 1;
            while (n < notes.size() && notes.get(n).getId() != id)
                n++;
            if (n == notes.size())
                return -1;
        } else if (n > id) {
            n = id - 1;
            while (n > -1 && notes.get(n).getId() != id)
                n--;
        }
        return n;
    }

    @Override
    public void deleteNote(int id) {
        int index = findIndexById(id);
        if (index == -1)
            handler.onError("No have item");
        notes.remove(index);
        handler.deleteNote(id);
    }

    @Override
    public void pushNote(int id) {
    }

    @Override
    public void addNote() {
        last_id++;
        BaseItem item = new BaseItem(last_id, "Note #" + notes.size() + ", id " + last_id,
                System.currentTimeMillis(), "Des #" + notes.size());
        notes.add(item);
        handler.addNote(item);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
