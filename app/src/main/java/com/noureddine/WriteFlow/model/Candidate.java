package com.noureddine.WriteFlow.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Candidate {

    @SerializedName("content")
    private Content content;

    @SerializedName("finishReason")
    private String finishReason;

    // Getters
    public Content getContent() { return content; }
    public String getFinishReason() { return finishReason; }





}
