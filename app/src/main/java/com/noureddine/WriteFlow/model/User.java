package com.noureddine.WriteFlow.model;

import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String email;
    private String uid;
    private String membership;
    private long endSubscription;
    private long wordPremium;
    private long wordProcessing;
//    String plane;


    public User() {}

    public User(String name, String email, String uid, String membership, long endSubscription, long wordPremium, long wordProcessing) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.membership = membership;
        this.endSubscription = endSubscription;
        this.wordPremium = wordPremium;
        this.wordProcessing = wordProcessing;
    }

//    public User(String name, String email, String uid, String membership, long endSubscription, long wordPremium, long wordProcessing, String plane) {
//        this.name = name;
//        this.email = email;
//        this.uid = uid;
//        this.membership = membership;
//        this.endSubscription = endSubscription;
//        this.wordPremium = wordPremium;
//        this.wordProcessing = wordProcessing;
//        this.plane = plane;
//    }

//    public String getPlane() {
//        return plane;
//    }
//
//    public void setPlane(String plane) {
//        this.plane = plane;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public long getEndSubscription() {
        return endSubscription;
    }

    public void setEndSubscription(long endSubscription) {
        this.endSubscription = endSubscription;
    }

    public long getWordPremium() {
        return wordPremium;
    }

    public void setWordPremium(long wordPremium) {
        this.wordPremium = wordPremium;
    }

    public long getWordProcessing() {
        return wordProcessing;
    }

    public void setWordProcessing(long wordProcessing) {
        this.wordProcessing = wordProcessing;
    }
}
