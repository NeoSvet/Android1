package ru.neosvet.notes.note;

import java.util.Calendar;

public class Item {
    private String title, description;
    private long date;

    public Item(String title, long date, String description) {
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public String getDateString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return int2str(c.get(Calendar.DAY_OF_MONTH)) + "." + int2str(c.get(Calendar.MONTH) + 1) + "." +
                c.get(Calendar.YEAR) + ", " + int2str(c.get(Calendar.HOUR_OF_DAY)) + ":" + int2str(c.get(Calendar.MINUTE));
    }

    public static String int2str(int i) {
        if (i > 9)
            return Integer.toString(i);
        else
            return "0" + i;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
