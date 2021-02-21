package ru.neosvet.notes.note;

public class CurrentBase {
    private static Base base = null;

    public static Base get() {
        if (base == null) {
            base = new RandomBase();
            base.open();
        }
        return base;
    }
}
