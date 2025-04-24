package com.noureddine.WriteFlow.model;


import java.util.List;

public class Candidate {
    private Content content;
    private String finishReason;
    private int index;
    private List<SafetyRating> safetyRatings;

    public Candidate(Content content, String finishReason, int index, List<SafetyRating> safetyRatings) {
        this.content = content;
        this.finishReason = finishReason;
        this.index = index;
        this.safetyRatings = safetyRatings;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getFinishReason() {
        return finishReason;
    }

    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<SafetyRating> getSafetyRatings() {
        return safetyRatings;
    }

    public void setSafetyRatings(List<SafetyRating> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }
}
