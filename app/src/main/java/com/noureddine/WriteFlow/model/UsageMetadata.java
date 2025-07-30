package com.noureddine.WriteFlow.model;

import com.google.gson.annotations.SerializedName;

public class UsageMetadata {
    @SerializedName("promptTokenCount")
    private int promptTokenCount;

    @SerializedName("candidatesTokenCount")
    private int candidatesTokenCount;

    @SerializedName("totalTokenCount")
    private int totalTokenCount;

    // Getters
    public int getPromptTokenCount() { return promptTokenCount; }
    public int getCandidatesTokenCount() { return candidatesTokenCount; }
    public int getTotalTokenCount() { return totalTokenCount; }
}
