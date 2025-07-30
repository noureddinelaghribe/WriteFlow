package com.noureddine.WriteFlow.model;

public class ProcessingWord {

    private String userId;
    private String type;
    private int promptTokens ;
    private int responseTokens ;
    private long timestamp;

    public ProcessingWord() {}


    public ProcessingWord(String userId, String type, int promptTokens, int responseTokens, long timestamp) {
        this.userId = userId;
        this.type = type;
        this.promptTokens = promptTokens;
        this.responseTokens = responseTokens;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getResponseTokens() {
        return responseTokens;
    }

    public void setResponseTokens(int responseTokens) {
        this.responseTokens = responseTokens;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
