package com.noureddine.WriteFlow.model;

import java.util.List;

public class PromptFeedback {
    private List<SafetyRating> safetyRatings;

    public PromptFeedback(List<SafetyRating> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }

    public List<SafetyRating> getSafetyRatings() {
        return safetyRatings;
    }

    public void setSafetyRatings(List<SafetyRating> safetyRatings) {
        this.safetyRatings = safetyRatings;
    }
}
