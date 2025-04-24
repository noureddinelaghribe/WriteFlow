//package com.noureddine.WriteFlow.model;
//
//import android.annotation.SuppressLint;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@SuppressLint("ParcelCreator")
//@Entity(tableName = "HistoryArticle")
//public class HistoryArticle implements Parcelable {
//
//    @PrimaryKey(autoGenerate = true)
//    int id;
//    String uid;
//    String response;
//    String article; //ai detector //grammer check
//    String type;
//    long date;
//
//    public HistoryArticle() {}
//
//    public HistoryArticle(String uid,String response, String type, long date) {
//        this.uid = uid;
//        this.response = response;
//        this.type = type;
//        this.date = date;
//    }
//
//    public HistoryArticle(String uid,String response, String type, String article, long date) {
//        this.uid = uid;
//        this.response = response;
//        this.article = article;
//        this.type = type;
//        this.date = date;
//    }
//
//    public String getResponse() {
//        return response;
//    }
//
//    public void setResponse(String response) {
//        this.response = response;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public long getDate() {
//        return date;
//    }
//
//    public void setData(long date) {
//        this.date = date;
//    }
//
//    public String getArticle() {
//        return article;
//    }
//
//    public void setArticle(String article) {
//        this.article = article;
//    }
//
//    public void setDate(long date) {
//        this.date = date;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel parcel, int i) {
//        parcel.writeInt(id);
//        parcel.writeString(uid);
//        parcel.writeString(response);
//        parcel.writeString(article);
//        parcel.writeString(type);
//        parcel.writeLong(date);
//    }
//
//    public static final Creator<HistoryArticle> CREATOR = new Creator<HistoryArticle>() {
//        @Override
//        public HistoryArticle createFromParcel(Parcel in) {
//            return new HistoryArticle(in);
//        }
//
//        @Override
//        public HistoryArticle[] newArray(int size) {
//            return new HistoryArticle[size];
//        }
//    };
//
//}


//
//package com.noureddine.WriteFlow.model;
//
//import android.annotation.SuppressLint;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//
//@SuppressLint("ParcelCreator")
//@Entity(tableName = "HistoryArticle")
//public class HistoryArticle implements Parcelable {
//
//    @PrimaryKey(autoGenerate = true)
//    int id;
//    String uid;
//    String response;
//    String article; // ai detector // grammer check
//    String type;
//    long date;
//    GrammarChecker grammarChecker;
//
//    public HistoryArticle() {}
//
//    public HistoryArticle(String uid, String response, String type, long date) {
//        this.uid = uid;
//        this.response = response;
//        this.type = type;
//        this.date = date;
//    }
//
//    public HistoryArticle(String uid, String response, String type, String article, long date) {
//        this.uid = uid;
//        this.response = response;
//        this.article = article;
//        this.type = type;
//        this.date = date;
//    }
//
//    public HistoryArticle(String uid, GrammarChecker grammarChecker, String type, String article, long date) {
//        this.uid = uid;
//        this.grammarChecker = grammarChecker;
//        this.article = article;
//        this.type = type;
//        this.date = date;
//    }
//
//    // منشئ لإعادة إنشاء الكائن من Parcel
//    protected HistoryArticle(Parcel in) {
//        id = in.readInt();
//        uid = in.readString();
//        response = in.readString();
//        article = in.readString();
//        type = in.readString();
//        date = in.readLong();
//    }
//
//    public String getResponse() {
//        return response;
//    }
//
//    public void setResponse(String response) {
//        this.response = response;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public long getDate() {
//        return date;
//    }
//
//    public void setData(long date) {
//        this.date = date;
//    }
//
//    public String getArticle() {
//        return article;
//    }
//
//    public void setArticle(String article) {
//        this.article = article;
//    }
//
//    public void setDate(long date) {
//        this.date = date;
//    }
//
//    public String getUid() {
//        return uid;
//    }
//
//    public void setUid(String uid) {
//        this.uid = uid;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public GrammarChecker getGrammarChecker() {
//        return grammarChecker;
//    }
//
//    public void setGrammarChecker(GrammarChecker grammarChecker) {
//        this.grammarChecker = grammarChecker;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel parcel, int flags) {
//        parcel.writeInt(id);
//        parcel.writeString(uid);
//        parcel.writeString(response);
//        parcel.writeString(article);
//        parcel.writeString(type);
//        parcel.writeLong(date);
//        parcel.writeParcelable( grammarChecker, flags);
//    }
//
//    // إضافة كائن CREATOR المطلوب
//    public static final Creator<HistoryArticle> CREATOR = new Creator<HistoryArticle>() {
//        @Override
//        public HistoryArticle createFromParcel(Parcel in) {
//            return new HistoryArticle(in);
//        }
//
//        @Override
//        public HistoryArticle[] newArray(int size) {
//            return new HistoryArticle[size];
//        }
//    };
//}




package com.noureddine.WriteFlow.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@SuppressLint("ParcelCreator")
@Entity(tableName = "HistoryArticle")
public class HistoryArticle implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    int id;
    private String uid;
    private String response;
    private String article; // ai detector // grammer check
    private String type;
    private long date;
    private GrammarChecker grammarChecker; // Ensure GrammarChecker is Parcelable

    private String operatorStr1 ;
    private String operatorStr2 ;
    private int optionSpinner;

    public HistoryArticle() {}

    public HistoryArticle(String uid, String response, String type, long date) {
        this.uid = uid;
        this.response = response;
        this.type = type;
        this.date = date;
    }

    public HistoryArticle(String uid, String response, String type, String article, long date) {
        this.uid = uid;
        this.response = response;
        this.article = article;
        this.type = type;
        this.date = date;
    }

    public HistoryArticle(String uid, GrammarChecker grammarChecker, String type, String article, long date) {
        this.uid = uid;
        this.grammarChecker = grammarChecker;
        this.article = article;
        this.type = type;
        this.date = date;
    }

    public HistoryArticle(String uid, String response, String article, String type, long date, String operatorStr1, String operatorStr2, int optionSpinner) {
        this.uid = uid;
        this.response = response;
        this.article = article;
        this.type = type;
        this.date = date;
        this.operatorStr1 = operatorStr1;
        this.operatorStr2 = operatorStr2;
        this.optionSpinner = optionSpinner;
    }

    public HistoryArticle(int id, String uid, String response, String article, String type, long date, String operatorStr1, String operatorStr2, int optionSpinner) {
        this.id = id;
        this.uid = uid;
        this.response = response;
        this.article = article;
        this.type = type;
        this.date = date;
        this.operatorStr1 = operatorStr1;
        this.operatorStr2 = operatorStr2;
        this.optionSpinner = optionSpinner;
    }

    // Constructor to recreate object from Parcel
    protected HistoryArticle(Parcel in) {
        id = in.readInt();
        uid = in.readString();
        response = in.readString();
        article = in.readString();
        type = in.readString();
        date = in.readLong();
        grammarChecker = in.readParcelable(GrammarChecker.class.getClassLoader());
        operatorStr1 = in.readString();
        operatorStr2 = in.readString();
        optionSpinner = in.readInt();
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setData(long date) {
        this.date = date;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GrammarChecker getGrammarChecker() {
        return grammarChecker;
    }

    public void setGrammarChecker(GrammarChecker grammarChecker) {
        this.grammarChecker = grammarChecker;
    }

    public String getOperatorStr1() {
        return operatorStr1;
    }

    public void setOperatorStr1(String operatorStr1) {
        this.operatorStr1 = operatorStr1;
    }

    public int getOptionSpinner() {
        return optionSpinner;
    }

    public void setOptionSpinner(int optionSpinner) {
        this.optionSpinner = optionSpinner;
    }

    public String getOperatorStr2() {
        return operatorStr2;
    }

    public void setOperatorStr2(String operatorStr2) {
        this.operatorStr2 = operatorStr2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(uid);
        parcel.writeString(response);
        parcel.writeString(article);
        parcel.writeString(type);
        parcel.writeLong(date);
        parcel.writeParcelable(grammarChecker, flags);
        parcel.writeString(operatorStr1);
        parcel.writeString(operatorStr2);
        parcel.writeInt(optionSpinner);

    }

    public static final Creator<HistoryArticle> CREATOR = new Creator<HistoryArticle>() {
        @Override
        public HistoryArticle createFromParcel(Parcel in) {
            return new HistoryArticle(in);
        }

        @Override
        public HistoryArticle[] newArray(int size) {
            return new HistoryArticle[size];
        }
    };
}
