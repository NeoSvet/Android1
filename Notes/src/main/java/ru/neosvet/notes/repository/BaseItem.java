package ru.neosvet.notes.repository;

import java.util.Calendar;

public class BaseItem {
    private String title, description, dateString = null;
    private int id;
    private long date;

    public BaseItem() {

    }

    public BaseItem(int id, String title, long date, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        initDateString();
    }

    private void initDateString() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        dateString = int2str(c.get(Calendar.DAY_OF_MONTH)) + "." + int2str(c.get(Calendar.MONTH) + 1) + "." +
                c.get(Calendar.YEAR) + ", " + int2str(c.get(Calendar.HOUR_OF_DAY)) + ":" + int2str(c.get(Calendar.MINUTE));
    }

    public int getId() {
        return id;
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
        if (dateString == null)
            initDateString();
        return dateString;
    }

    public static String int2str(int i) {
        if (i > 9)
            return Integer.toString(i);
        else
            return "0" + i;
    }

    public void setDate(long date) {
        this.date = date;
        initDateString();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
