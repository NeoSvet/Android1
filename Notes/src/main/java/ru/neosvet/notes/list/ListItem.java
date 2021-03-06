package ru.neosvet.notes.list;

public class ListItem {
    private String title, subtitle;
    private int id;

    public ListItem(int id, String title, String subtitle) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
