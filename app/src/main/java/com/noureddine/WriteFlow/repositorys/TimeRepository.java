package com.noureddine.WriteFlow.repositorys;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.noureddine.WriteFlow.interfaces.TimeApiService;
import com.noureddine.WriteFlow.model.TimeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TimeRepository {
    private static final String BASE_URL = "https://timeapi.io/";
    private TimeApiService apiService;
    private static TimeRepository instance;

    private TimeRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(TimeApiService.class);
    }

    public static synchronized TimeRepository getInstance() {
        if (instance == null) {
            instance = new TimeRepository();
        }
        return instance;
    }

    public LiveData<TimeResponse> getTimeZoneData() {
        final MutableLiveData<TimeResponse> data = new MutableLiveData<>();

        apiService.getTimeZone().enqueue(new Callback<TimeResponse>() {
            @Override
            public void onResponse(Call<TimeResponse> call, Response<TimeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }
                Log.d("TimeRepository", "onResponse: "+response);
            }

            @Override
            public void onFailure(Call<TimeResponse> call, Throwable t) {
                // Handle error here (you can post a null or use another LiveData for errors)
                data.postValue(null);
                Log.d("TimeRepository", "onResponse: "+t.getMessage());
            }
        });
        return data;
    }

}
