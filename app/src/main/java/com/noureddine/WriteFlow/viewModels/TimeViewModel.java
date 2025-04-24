package com.noureddine.WriteFlow.viewModels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.noureddine.WriteFlow.model.TimeResponse;
import com.noureddine.WriteFlow.repositorys.TimeRepository;

public class TimeViewModel extends AndroidViewModel {

    private TimeRepository repository;
    private LiveData<TimeResponse> timeZoneData;

    public TimeViewModel(@NonNull Application application) {
        super(application);
        repository = TimeRepository.getInstance();
        timeZoneData = repository.getTimeZoneData();
    }

    public LiveData<TimeResponse> getTimeZoneData() {
        return timeZoneData;
    }
}

