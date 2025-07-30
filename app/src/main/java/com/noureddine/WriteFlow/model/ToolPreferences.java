package com.noureddine.WriteFlow.model;

public class ToolPreferences {
    private String paraphraserModel; // "openai" or "gemini"
    private String grammarCheckerModel; // "openai" or "gemini"
    private String aiDetectorModel; // "openai" or "gemini"
    private String paragraphGeneratorModel; // "openai" or "gemini"
    private String summarizerModel; // "openai" or "gemini"
    private String smartTranslationModel; // "openai" or "gemini"
    private String plagiarismCheckingModel; // "openai" or "gemini"

    public ToolPreferences() {
    }

    public ToolPreferences(String paraphraserModel, String grammarCheckerModel, String aiDetectorModel, String paragraphGeneratorModel,
                           String summarizerModel, String smartTranslationModel, String plagiarismCheckingModel) {
        this.paraphraserModel = paraphraserModel;
        this.grammarCheckerModel = grammarCheckerModel;
        this.aiDetectorModel = aiDetectorModel;
        this.paragraphGeneratorModel = paragraphGeneratorModel;
        this.summarizerModel = summarizerModel;
        this.smartTranslationModel = smartTranslationModel;
        this.plagiarismCheckingModel = plagiarismCheckingModel;
    }

    public String getParaphraserModel() {
        return paraphraserModel;
    }

    public void setParaphraserModel(String paraphraserModel) {
        this.paraphraserModel = paraphraserModel;
    }

    public String getGrammarCheckerModel() {
        return grammarCheckerModel;
    }

    public void setGrammarCheckerModel(String grammarCheckerModel) {
        this.grammarCheckerModel = grammarCheckerModel;
    }

    public String getAiDetectorModel() {
        return aiDetectorModel;
    }

    public void setAiDetectorModel(String aiDetectorModel) {
        this.aiDetectorModel = aiDetectorModel;
    }

    public String getParagraphGeneratorModel() {
        return paragraphGeneratorModel;
    }

    public void setParagraphGeneratorModel(String paragraphGeneratorModel) {
        this.paragraphGeneratorModel = paragraphGeneratorModel;
    }

    public String getSummarizerModel() {
        return summarizerModel;
    }

    public void setSummarizerModel(String summarizerModel) {
        this.summarizerModel = summarizerModel;
    }

    public String getSmartTranslationModel() {
        return smartTranslationModel;
    }

    public void setSmartTranslationModel(String smartTranslationModel) {
        this.smartTranslationModel = smartTranslationModel;
    }

    public String getPlagiarismCheckingModel() {
        return plagiarismCheckingModel;
    }

    public void setPlagiarismCheckingModel(String plagiarismCheckingModel) {
        this.plagiarismCheckingModel = plagiarismCheckingModel;
    }
}
