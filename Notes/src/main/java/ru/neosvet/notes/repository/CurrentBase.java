package ru.neosvet.notes.repository;

public class CurrentBase {
    private static Base base = null;

    public static Base get() {
        if (base == null) {
            base = new RandomBase();
            base.open();
        } else if (base.isClosed())
            base.open();
        return base;
    }
}
