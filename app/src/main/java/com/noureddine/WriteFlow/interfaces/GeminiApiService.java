package com.noureddine.WriteFlow.interfaces;


import com.noureddine.WriteFlow.model.GeminiRequest;
import com.noureddine.WriteFlow.model.GeminiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {
    @POST("v1beta/models/gemini-2.5-flash-preview-04-17:generateContent") //gemini-2.0-flash
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );

}
