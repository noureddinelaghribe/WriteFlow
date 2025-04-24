package com.noureddine.WriteFlow.model;

public class Tool {
    private int img;
    private String text;

    public Tool(int img, String text) {
        this.img = img;
        this.text = text;
    }

    public Tool() {}

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
