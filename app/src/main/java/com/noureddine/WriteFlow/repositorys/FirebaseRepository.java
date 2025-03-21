package com.noureddine.WriteFlow.repositorys;

import com.google.firebase.database.DatabaseReference;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noureddine.WriteFlow.Utils.EncryptedPrefsManager;
import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.model.User;

import java.util.Objects;

public class FirebaseRepository {
    private static final String TAG = "FirebaseRepository";
    private DatabaseReference databaseReference;
    EncryptedPrefsManager prefs;

    public FirebaseRepository(Context context) {
        // Get a reference to the "data" node in your Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        prefs = EncryptedPrefsManager.getInstance(context);
    }

    // Save a simple key-value pair to the DatabaseArticle
    public void saveUser(User user) {
        databaseReference.child("Users").child(prefs.getUser().getUid()).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Data saved successfully for key: " + prefs.getUser().getUid());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving data for key: " + prefs.getUser().getUid(), e);
                });
    }

    // Save a simple key-value pair to the DatabaseArticle
    public void ProcessingAnalytics(String type) {
        String key = databaseReference.push().getKey(); ;
        databaseReference.child("ProcessingAnalytics").child(key).child(prefs.getUser().getUid()).setValue(type)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "ProcessingAnalytics saved successfully for key: " + prefs.getUser().getUid());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving ProcessingAnalytics for key: " + prefs.getUser().getUid(), e);
                });
    }

    // Retrieve data from the DatabaseArticle using a key
    public void getUser(String key, ValueEventListener listener) {
        databaseReference.child("Users").child(key).addListenerForSingleValueEvent(listener);
    }
}

