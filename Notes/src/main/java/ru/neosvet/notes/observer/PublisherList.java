package ru.neosvet.notes.observer;

import java.util.ArrayList;
import java.util.List;

import ru.neosvet.notes.repository.Note;

public class PublisherList {
    private static List<ObserverList> observers = new ArrayList<>();

    public static void subscribe(ObserverList observer) {
        observers.add(observer);
    }

    public static void unsubscribe(ObserverList observer) {
        observers.remove(observer);
    }

    public static void notify(List<Note> list) {
        for (ObserverList observer : observers) {
            observer.putList(list);
        }
    }

    public static void addItem(Note note) {
        for (ObserverList observer : observers) {
            observer.addItem(note);
        }
    }
}
