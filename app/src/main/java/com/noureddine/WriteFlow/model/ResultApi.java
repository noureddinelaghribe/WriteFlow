package com.noureddine.WriteFlow.model;

public class ResultApi {

    private String result;
    private int promptTokens;
    private int candidatesTokens;

    public ResultApi() {}

    public ResultApi(String result, int promptTokens, int candidatesTokens) {
        this.result = result;
        this.promptTokens = promptTokens;
        this.candidatesTokens = candidatesTokens;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getCandidatesTokens() {
        return candidatesTokens;
    }

    public void setCandidatesTokens(int candidatesTokens) {
        this.candidatesTokens = candidatesTokens;
    }
}
