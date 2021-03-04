package ru.neosvet.notes.observer;

import java.util.ArrayList;
import java.util.List;

public class PublisherNote {
    private static List<ObserverNote> observers = new ArrayList<>();
    private static String title = null, description = null;
    private static int id = -1;
    private static long date = -1;
    private static boolean deleted = false;

    public static void subscribe(ObserverNote observer) {
        observers.add(observer);
    }

    public static void unsubscribe(ObserverNote observer) {
        observers.remove(observer);
    }

    public static void notifyDate(int id, long date) {
        if (PublisherNote.id != id)
            clear();
        PublisherNote.id = id;
        PublisherNote.date = date;
        for (ObserverNote observer : observers) {
            observer.updateDate(id, date);
        }
    }

    public static void notifyContent(int id, String title, String description) {
        if (PublisherNote.id != id)
            clear();
        PublisherNote.id = id;
        PublisherNote.title = title;
        PublisherNote.description = description;
        for (ObserverNote observer : observers) {
            observer.updateContent(id, title, description);
        }
    }

    public static void notifyDelete(int id) {
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
        PublisherNote.date = -1;
        PublisherNote.title = null;
        PublisherNote.description = null;
        PublisherNote.deleted = false;
    }

    public static void queryChanges(ObserverNote observer) {
        if (deleted) {
            observer.delete(id);
            return;
        }
        if (date != -1)
            observer.updateDate(id, date);
        if (title != null)
            observer.updateContent(id, title, description);
    }
}
