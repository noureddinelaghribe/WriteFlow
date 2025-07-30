package com.noureddine.WriteFlow.model;


public class DocxtractResponse {
    private String status;
    private String message;
    private DocxtractData data;

    // Getters and setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public DocxtractData getData() { return data; }
    public void setData(DocxtractData data) { this.data = data; }
}

