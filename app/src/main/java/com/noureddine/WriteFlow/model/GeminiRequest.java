package com.noureddine.WriteFlow.model;


import java.util.ArrayList;
import java.util.List;

public class GeminiRequest {

//    private List<Content> contents;
//
//    public GeminiRequest(String prompt) {
//        if (prompt == null) {
//            prompt = "";  // Handle null case
//        }
//
//        Part part = new Part(prompt);
//        List<Part> parts = new ArrayList<>();
//        parts.add(part);
//
//        Content content = new Content(parts, "user");
//        this.contents = new ArrayList<>();
//        this.contents.add(content);
//    }


    private SystemInstruction systemInstruction;
    private List<Content> contents;


    public GeminiRequest(SystemInstruction systemInstruction, List<Content> contents) {
        this.systemInstruction = systemInstruction;
        this.contents = contents;
    }

    public GeminiRequest(List<Content> contents) {
        this.contents = contents;
    }

    public GeminiRequest(SystemInstruction systemInstruction) {
        this.systemInstruction = systemInstruction;
    }

    public SystemInstruction getSystemInstruction() {
        return systemInstruction;
    }

    public void setSystemInstruction(SystemInstruction systemInstruction) {
        this.systemInstruction = systemInstruction;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }
}

