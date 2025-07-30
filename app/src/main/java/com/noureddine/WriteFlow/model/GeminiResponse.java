package com.noureddine.WriteFlow.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeminiResponse {


    @SerializedName("candidates")
    private List<Candidate> candidates;

    @SerializedName("usageMetadata")
    private UsageMetadata usageMetadata;

    // Getters
    public List<Candidate> getCandidates() { return candidates; }
    public UsageMetadata getUsageMetadata() { return usageMetadata; }




}


