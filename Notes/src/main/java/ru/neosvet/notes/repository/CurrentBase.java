package ru.neosvet.notes.repository;

public class CurrentBase {
    private static Base base = null;

    public static void init(BaseHandler handler) {
        if (base == null)
            base = new FireBase();
        else if (!base.isClosed())
            base.close();
        base.open(handler);
    }

    public static Base get() {
        //if (base == null || base.isClosed())
        //    throw new Exception("Base is not initialized.");
        return base;
    }
}
