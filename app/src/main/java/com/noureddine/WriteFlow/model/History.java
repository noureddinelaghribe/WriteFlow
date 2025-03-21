package com.noureddine.WriteFlow.model;

public class History {

    String date;
    String text;

    public History() {}

    public History(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
