//package com.noureddine.phraseflow.interfaces;
//
//
//import com.noureddine.phraseflow.model.DeepSeekRequest;
//import com.noureddine.phraseflow.model.DeepSeekResponse;
//
//import retrofit2.Call;
//import retrofit2.http.Body;
//import retrofit2.http.Header;
//import retrofit2.http.POST;
//
//public interface DeepSeekApiService {
//    @POST("v1/chat/completions")
//    Call<DeepSeekResponse> getChatCompletion(
//            @Header("Authorization") String apiKey,
//            @Body DeepSeekRequest request
//    );
//}
