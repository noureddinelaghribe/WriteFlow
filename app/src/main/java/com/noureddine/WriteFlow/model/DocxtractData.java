package com.noureddine.WriteFlow.model;

public class DocxtractData {
    private String extractedText;
    private String documentType;
    private int pageCount;

    // Getters and setters
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public int getPageCount() { return pageCount; }
    public void setPageCount(int pageCount) { this.pageCount = pageCount; }
}
