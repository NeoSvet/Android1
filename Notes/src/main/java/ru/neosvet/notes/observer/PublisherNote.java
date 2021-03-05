package ru.neosvet.notes.observer;

import java.util.ArrayList;
import java.util.List;

import ru.neosvet.notes.repository.BaseItem;

public class PublisherNote {
    private static List<ObserverNote> observers = new ArrayList<>();
    private static BaseItem note = null;
    private static int id = -1;
    private static boolean deleted = false;

    public static void subscribe(ObserverNote observer) {
        observers.add(observer);
    }

    public static void unsubscribe(ObserverNote observer) {
        observers.remove(observer);
    }

    public static void notifyNote(BaseItem note) {
        if (PublisherNote.id != note.getId())
            clear();
        PublisherNote.id = note.getId();
        PublisherNote.note = note;
        for (ObserverNote observer : observers) {
            observer.updateNote(note);
        }
    }

    public static void runDelete(int id) {
        clear();
        PublisherNote.id = id;
        PublisherNote.deleted = true;
        for (ObserverNote observer : observers) {
            observer.delete(id);
        }
    }

    public static void cancelDelete(int id) {
        if (PublisherNote.id != id)
            return;
        PublisherNote.deleted = false;
    }

    public static void clear() {
        PublisherNote.id = -1;
        PublisherNote.note = null;
        PublisherNote.deleted = false;
    }

    public static void queryChanges(ObserverNote observer) {
        if (deleted) {
            observer.delete(id);
            return;
        }
        if (id != -1)
            observer.updateNote(note);
    }
}
