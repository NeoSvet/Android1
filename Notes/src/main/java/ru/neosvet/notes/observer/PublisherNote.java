package ru.neosvet.notes.observer;

import java.util.ArrayList;
import java.util.List;

public class PublisherNote {
    private static List<ObserverNote> observers = new ArrayList<>();
    private static String title = null, description = null;
    private static int id;
    private static long date = -1;

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

    private static void clear() {
        PublisherNote.date = -1;
        PublisherNote.title = null;
        PublisherNote.description = null;
    }

    public static void queryChanges(ObserverNote observer) {
        if (date != -1)
            observer.updateDate(id, date);
        if (title != null)
            observer.updateContent(id, title, description);
    }
}
