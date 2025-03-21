//package com.noureddine.WriteFlow.model;
//
//public class GrammarChecker {
//
//    String text;
//    String issue;
//
//    public GrammarChecker() {
//    }
//
//    public GrammarChecker(GrammarChecker grammarChecker) {
//        this.text = grammarChecker.getText();
//        this.issue = grammarChecker.getIssue();
//    }
//
//    public GrammarChecker(String text, String issue) {
//        this.text = text;
//        this.issue = issue;
//    }
//
//
//    public String getText() {
//        return text;
//    }
//
//    public void setText(String text) {
//        this.text = text;
//    }
//
//    public String getIssue() {
//        return issue;
//    }
//
//    public void setIssue(String issue) {
//        this.issue = issue;
//    }
//}



package com.noureddine.WriteFlow.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GrammarChecker implements Parcelable {

    String text;
    String issue;

    public GrammarChecker() {}

    public GrammarChecker(GrammarChecker grammarChecker) {
        this.text = grammarChecker.getText();
        this.issue = grammarChecker.getIssue();
    }

    public GrammarChecker(String text, String issue) {
        this.text = text;
        this.issue = issue;
    }

    // Constructor to recreate object from Parcel
    protected GrammarChecker(Parcel in) {
        text = in.readString();
        issue = in.readString();
    }

    // CREATOR for Parcelable implementation
    public static final Creator<GrammarChecker> CREATOR = new Creator<GrammarChecker>() {
        @Override
        public GrammarChecker createFromParcel(Parcel in) {
            return new GrammarChecker(in);
        }

        @Override
        public GrammarChecker[] newArray(int size) {
            return new GrammarChecker[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(issue);
    }
}
