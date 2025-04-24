package com.noureddine.WriteFlow.model;

public class Close {
    private boolean closeEnabled;
    private String completionTime;
    private String contact;

    public Close() {
    }

    public Close(boolean closeEnabled) {
        this.closeEnabled = closeEnabled;
    }

    public Close(boolean closeEnabled, String completionTime, String contact) {
        this.closeEnabled = closeEnabled;
        this.completionTime = completionTime;
        this.contact = contact;
    }

    public boolean isCloseEnabled() {
        return closeEnabled;
    }

    public void setCloseEnabled(boolean closeEnabled) {
        this.closeEnabled = closeEnabled;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
