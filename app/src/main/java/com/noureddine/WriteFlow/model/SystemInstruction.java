package com.noureddine.WriteFlow.model;

import java.util.List;

public class SystemInstruction {
    private List<Part> parts;

    public SystemInstruction(List<Part> parts) {
        this.parts = parts;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
}
