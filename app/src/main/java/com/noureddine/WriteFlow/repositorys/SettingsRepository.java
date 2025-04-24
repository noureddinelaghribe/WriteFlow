package com.noureddine.WriteFlow.repositorys;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.noureddine.WriteFlow.model.Close;
import com.noureddine.WriteFlow.model.Settings;
import com.noureddine.WriteFlow.model.ToolPreferences;
import com.noureddine.WriteFlow.model.Update;

public class SettingsRepository {
    private static final String SETTINGS_REF = "settings";
    private final DatabaseReference settingsRef;
    private final MutableLiveData<Settings> settingsLiveData = new MutableLiveData<>();

    public SettingsRepository() {
        settingsRef = FirebaseDatabase.getInstance().getReference(SETTINGS_REF);
        loadSettings();
    }

    private void loadSettings() {
        settingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Settings settings = dataSnapshot.getValue(Settings.class);
                if (settings == null) {
                    // Initialize with default settings
                    settings = new Settings(new Update(), new Close(), new ToolPreferences());
                    saveSettings(settings);
                }
                settingsLiveData.setValue(settings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("SettingsRepository", "Failed to load settings", databaseError.toException());
            }
        });
    }

    public void saveSettings(Settings settings) {
        settingsRef.setValue(settings)
                .addOnSuccessListener(aVoid -> Log.d("SettingsRepository", "Settings saved successfully"))
                .addOnFailureListener(e -> Log.e("SettingsRepository", "Failed to save settings", e));
    }

    public LiveData<Settings> getSettingsLiveData() {
        return settingsLiveData;
    }
}

