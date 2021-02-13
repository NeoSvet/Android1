package ru.neosvet.notes.note;

public class Item {
    private String title, date, des;

    public Item(String title, String date, String des) {
        this.title = title;
        this.date = date;
        this.des = des;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
