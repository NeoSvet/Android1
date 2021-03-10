package ru.neosvet.notes.observer;

import java.util.ArrayList;
import java.util.List;

import ru.neosvet.notes.repository.Note;

public class PublisherNote {
    private static final String NULL = "null";
    private static List<ObserverNote> observers = new ArrayList<>();
    private static Note note = null;
    private static String id = NULL;
    private static boolean deleted = false;

    public static void subscribe(ObserverNote observer) {
        observers.add(observer);
    }

    public static void unsubscribe(ObserverNote observer) {
        observers.remove(observer);
    }

    public static void notify(Note note) {
        if (!PublisherNote.id.equals(note.getId()))
            clear();
        PublisherNote.id = note.getId();
        PublisherNote.note = note;
        for (ObserverNote observer : observers) {
            observer.updateNote(note);
        }
    }

    public static void deleted(String id) {
        clear();
        PublisherNote.id = id;
        PublisherNote.deleted = true;
        for (ObserverNote observer : observers) {
            observer.deletedNote(id);
        }
    }

    public static void cancelDelete(String id) {
        if (!PublisherNote.id.equals(id))
            return;
        PublisherNote.deleted = false;
    }

    public static void clear() {
        PublisherNote.id = NULL;
        PublisherNote.note = null;
        PublisherNote.deleted = false;
    }

    public static void queryChanges(ObserverNote observer) {
        if (deleted) {
            observer.deletedNote(id);
            return;
        }
        if (!id.equals(NULL))
            observer.updateNote(note);
    }
}
