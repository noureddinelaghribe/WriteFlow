package com.noureddine.WriteFlow.model;

public class ExtractRequest {
    private String url;
    private String format;

    public ExtractRequest(String url, String format) {
        this.url = url;
        this.format = format;
    }

    // Getters and setters
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}
