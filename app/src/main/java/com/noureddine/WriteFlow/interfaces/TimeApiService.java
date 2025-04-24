package com.noureddine.WriteFlow.interfaces;

import com.noureddine.WriteFlow.model.TimeResponse;
import retrofit2.Call;
import retrofit2.http.GET;


public interface TimeApiService {
    @GET("api/time/current/zone?timeZone=Europe%2FAmsterdam")
    Call<TimeResponse> getTimeZone();
}
