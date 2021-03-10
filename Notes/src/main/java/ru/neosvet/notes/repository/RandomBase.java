package ru.neosvet.notes.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class RandomBase implements Base {
    private int last_id = -1;
    private List<Note> notes = new ArrayList<>();
    private BaseCallbacks callbacks;

    @Override
    public void open(BaseCallbacks callbacks) {
        this.callbacks = callbacks;
        loadNextPage();
    }

    @Override
    public void loadNextPage() {
        callbacks.listIsReady();
    }

    @Override
    public Note[] getList(int offset) {
        int limit = LIMIT;
        if (offset + limit > notes.size())
            generateTo(offset + limit);
        Note[] m = new Note[limit];
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
            notes.add(new Note(last_id, "Note #" + i + ", id " + last_id,
                    date.getTimeInMillis(), "Des #" + i));
        }
    }

    @Override
    public Note getNote(int id) {
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
        if (index == -1) {
            callbacks.onError("Item is not exists");
            return;
        }
        notes.remove(index);
        callbacks.deleteNote(id);
    }

    @Override
    public void pushNote(Note note) {
        int index = findIndexById(note.getId());
        if (index == -1) {
            callbacks.onError("Item is not exists");
            return;
        }
        notes.get(index).setTitle(note.getTitle());
        notes.get(index).setDate(note.getDate());
        notes.get(index).setDescription(note.getDescription());
        callbacks.updateNote(note);
    }

    @Override
    public void addNote() {
        last_id++;
        Note item = new Note(last_id, "Note #" + notes.size() + ", id " + last_id,
                System.currentTimeMillis(), "Des #" + notes.size());
        notes.add(item);
        callbacks.addNote(item);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
