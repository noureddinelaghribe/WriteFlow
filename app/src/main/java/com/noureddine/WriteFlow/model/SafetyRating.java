package com.noureddine.WriteFlow.model;

public class SafetyRating {

    private String category;
    private String probability;

    public SafetyRating(String category, String probability) {
        this.category = category;
        this.probability = probability;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability;
    }
}
