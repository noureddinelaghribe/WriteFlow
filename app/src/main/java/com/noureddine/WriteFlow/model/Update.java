package com.noureddine.WriteFlow.model;

public class Update {
    private boolean updateEnabled;
    private String verstion;
    private String whatsNew;
    private String link;

    public Update() {
    }

    public Update(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;
    }

    public Update(boolean updateEnabled, String verstion, String whatsNew, String link) {
        this.updateEnabled = updateEnabled;
        this.verstion = verstion;
        this.whatsNew = whatsNew;
        this.link = link;
    }

    public boolean isUpdateEnabled() {
        return updateEnabled;
    }

    public void setUpdateEnabled(boolean updateEnabled) {
        this.updateEnabled = updateEnabled;
    }

    public String getVerstion() {
        return verstion;
    }

    public void setVerstion(String verstion) {
        this.verstion = verstion;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
