package com.noureddine.WriteFlow.viewModels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.noureddine.WriteFlow.model.Candidate;
import com.noureddine.WriteFlow.model.GeminiResponse;
import com.noureddine.WriteFlow.model.ResultApi;
import com.noureddine.WriteFlow.model.TypeProcessing;
import com.noureddine.WriteFlow.repositorys.GeminiRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeminiViewModel extends ViewModel {
    private final GeminiRepository repository;
    private final MutableLiveData<ResultApi> resultApi = new MutableLiveData<>();
    //private final MutableLiveData<String> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    //private TextRepository repository;
    //private MutableLiveData<String> extractedText = new MutableLiveData<>();
    //private MutableLiveData<Integer> promptTokenCount = new MutableLiveData<>();
    //private MutableLiveData<Integer> candidatesTokenCount = new MutableLiveData<>();
    //private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    //private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public GeminiViewModel() {
        // Use a secure method to store your API key in production
        //repository = new GeminiRepository(EncryptionManager.decryptText("/XC91qeJbKTl9sfHU0D1B65T51dJ5unzR+rCpl7/BbE7IDGZpIQ0P7LE3g6R4R4q")); // Store securely);
        repository = new GeminiRepository("AIzaSyBWWI3T58CrXE5Ffv5--JIA-OvijuUCRm8"); // Store securely);


    }

    public void generateContent(TypeProcessing typeProcessing) {
        isLoading.setValue(true);
        error.setValue(null);

        repository.sendTodo( typeProcessing, new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    //result.postValue(response.body().getCandidates().get(0).getContent().getParts().get(0).getText());



                    String text = extractTextFromResponse(response.body());
                    int promptTokens = response.body().getUsageMetadata().getPromptTokenCount();
                    int candidatesTokens = response.body().getUsageMetadata().getCandidatesTokenCount();

                    resultApi.postValue(new ResultApi(text,promptTokens,candidatesTokens));


//                    // استخراج معلومات الرموز
//                    if (response.body().getUsageMetadata() != null) {
//                        promptTokenCount.setValue(response.body().getUsageMetadata().getPromptTokenCount());
//                        candidatesTokenCount.setValue(response.body().getUsageMetadata().getCandidatesTokenCount());
//                    }



                    Log.d("TAG", "response.isSuccessful() && response.body() != null response : "+response.body());



                } else {
                    error.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });

    }

    public void convertText2Html(String text){
        isLoading.setValue(true);
        error.setValue(null);

        repository.convertText2Html(text, new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {

                    String text = extractTextFromResponse(response.body());
                    int promptTokens = response.body().getUsageMetadata().getPromptTokenCount();
                    int candidatesTokens = response.body().getUsageMetadata().getCandidatesTokenCount();

                    resultApi.postValue(new ResultApi(text,promptTokens,candidatesTokens));

//                    resultApi.postValue(
//                            response.body().getCandidates().get(0).getContent().getParts().get(0).getText()
//                            );

                } else {
                    error.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                isLoading.postValue(false);
                error.postValue("Network error: " + t.getMessage());
            }
        });

    }

    // Getters for LiveData
    //public LiveData<String> getResult() { return result; }
    public LiveData<ResultApi> getResultApi() { return resultApi; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }


    // استخراج النص من الاستجابة
    private String extractTextFromResponse(GeminiResponse response) {
        if (response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            Candidate candidate = response.getCandidates().get(0);
            if (candidate.getContent() != null &&
                    candidate.getContent().getParts() != null &&
                    !candidate.getContent().getParts().isEmpty()) {

                return candidate.getContent().getParts().get(0).getText();
            }
        }
        return "";
    }


}