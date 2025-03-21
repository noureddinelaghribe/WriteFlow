//package com.noureddine.phraseflow.repositorys;
//
//import com.noureddine.phraseflow.interfaces.DeepSeekApiService;
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//    private static final String BASE_URL = "https://api.deepseek.com/";
//    private static DeepSeekApiService apiService;
//
//    public static DeepSeekApiService getApiService() {
//        if (apiService == null) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//            OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                    .addInterceptor(loggingInterceptor)
//                    .connectTimeout(120, TimeUnit.SECONDS)
//                    .readTimeout(120, TimeUnit.SECONDS)
//                    .writeTimeout(120, TimeUnit.SECONDS)
//                    .build();
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(okHttpClient)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            apiService = retrofit.create(DeepSeekApiService.class);
//        }
//
//        return apiService;
//    }
//}