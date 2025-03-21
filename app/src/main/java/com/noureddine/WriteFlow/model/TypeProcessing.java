package com.noureddine.WriteFlow.model;

public class TypeProcessing {

    String text;
    String type;
    String language;
    String mode;
    String keywords;

    public TypeProcessing(String text, String type, String language, String mode, String keywords) {
        this.text = text;
        this.type = type;
        this.language = language;
        this.mode = mode;
        this.keywords = keywords;
    }

    public TypeProcessing() {}

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
