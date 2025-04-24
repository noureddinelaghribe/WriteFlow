package com.noureddine.WriteFlow.model;

import java.util.List;

public class GeminiResponse {

    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;

    public GeminiResponse(List<Candidate> candidates, PromptFeedback promptFeedback) {
        this.candidates = candidates;
        this.promptFeedback = promptFeedback;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    public PromptFeedback getPromptFeedback() {
        return promptFeedback;
    }

    public void setPromptFeedback(PromptFeedback promptFeedback) {
        this.promptFeedback = promptFeedback;
    }
}


