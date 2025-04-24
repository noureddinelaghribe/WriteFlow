package com.noureddine.WriteFlow.viewModels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.noureddine.WriteFlow.Utils.EncryptionManager;
import com.noureddine.WriteFlow.model.GeminiResponse;
import com.noureddine.WriteFlow.model.TypeProcessing;
import com.noureddine.WriteFlow.repositorys.GeminiRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeminiViewModel extends ViewModel {
    private final GeminiRepository repository;
    private final MutableLiveData<String> result = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public GeminiViewModel() {
        // Use a secure method to store your API key in production
        repository = new GeminiRepository(EncryptionManager.decryptText("/XC91qeJbKTl9sfHU0D1B65T51dJ5unzR+rCpl7/BbE7IDGZpIQ0P7LE3g6R4R4q")); // Store securely);
        Log.d("TAG", "GeminiViewModel: "+ EncryptionManager.decryptText("AIzaSyBLqDh9fykYZjobRW_4una4VqMF0GteSLw"));

    }

    public void generateContent(TypeProcessing typeProcessing) {
        isLoading.setValue(true);
        error.setValue(null);

        repository.sendTodo( typeProcessing, new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    result.postValue(response.body().getCandidates().get(0).getContent().getParts().get(0).getText());
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
    public LiveData<String> getResult() { return result; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return error; }
}