package ru.neosvet.notes.exchange;

public class PublisherDate {
    private static ObserverDate observer;

    public static void subscribe(ObserverDate observer) {
        PublisherDate.observer = observer;
    }

    public static void unsubscribe() {
        observer = null;
    }

    public static void notify(long date) {
        if (observer != null)
            observer.updateDate(date);
    }
}
