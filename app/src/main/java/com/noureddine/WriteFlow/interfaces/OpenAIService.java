package com.noureddine.WriteFlow.interfaces;

import com.noureddine.WriteFlow.model.ChatRequest;
import com.noureddine.WriteFlow.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

// OpenAIService.java
public interface OpenAIService {
    @POST("chat/completions")
    Call<ChatResponse> createChatCompletion(
            @Header("Authorization") String authorization,
            @Body ChatRequest request
    );
}
