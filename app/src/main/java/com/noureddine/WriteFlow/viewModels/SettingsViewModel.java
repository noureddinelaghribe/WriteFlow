package com.noureddine.WriteFlow.viewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.noureddine.WriteFlow.model.Settings;
import com.noureddine.WriteFlow.repositorys.SettingsRepository;


public class SettingsViewModel extends AndroidViewModel {
    private final SettingsRepository repository;
    private final LiveData<Settings> settingsLiveData;

    public SettingsViewModel(Application application) {
        super(application);
        repository = new SettingsRepository();
        settingsLiveData = repository.getSettingsLiveData();
    }

    public LiveData<Settings> getSettings() {
        return settingsLiveData;
    }

}
