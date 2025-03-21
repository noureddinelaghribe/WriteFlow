package com.noureddine.WriteFlow.model;

import java.util.List;

public class ChatRequest {
    //private String model = "gpt-3.5-turbo";
    private String model = "gpt-4o-mini";
    private List<Message> messages;

    public ChatRequest(List<Message> messages) {
        this.messages = messages;
    }

    // Getters and setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}