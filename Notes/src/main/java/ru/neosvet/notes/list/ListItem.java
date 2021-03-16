package ru.neosvet.notes.list;

public class ListItem {
    private String title, subtitle;

    public ListItem(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
