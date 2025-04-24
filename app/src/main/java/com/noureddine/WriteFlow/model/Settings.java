package com.noureddine.WriteFlow.model;

import com.google.firebase.database.PropertyName;

public class Settings {
    @PropertyName("Update")
    private Update update;
    @PropertyName("Close")
    private Close close;
    private ToolPreferences toolPreferences;

    public Settings() {
    }

    public Settings(Update update, Close close, ToolPreferences toolPreferences) {
        this.update = update;
        this.close = close;
        this.toolPreferences = toolPreferences;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public Close getClose() {
        return close;
    }

    public void setClose(Close close) {
        this.close = close;
    }

    public ToolPreferences getToolPreferences() {
        return toolPreferences;
    }

    public void setToolPreferences(ToolPreferences toolPreferences) {
        this.toolPreferences = toolPreferences;
    }
}
