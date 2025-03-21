package com.noureddine.WriteFlow.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.noureddine.WriteFlow.model.TypeProcessing;
import com.noureddine.WriteFlow.repositorys.ChatRepository;

// ChatViewModel.java
public class ChatViewModel extends ViewModel {
    private final ChatRepository repository;
    private final MutableLiveData<String> responseLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ChatViewModel() {
        repository = new ChatRepository();
    }

    public LiveData<String> getResponseLiveData() {
        return responseLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void sendMessage(TypeProcessing typeProcessing) {
        loadingLiveData.setValue(true);

        repository.sendTodo(typeProcessing, new ChatRepository.ChatCallback() {
            @Override
            public void onSuccess(String response) {
                responseLiveData.postValue(response);
                loadingLiveData.postValue(false);
            }

            @Override
            public void onError(String errorMessage) {
                errorLiveData.postValue(errorMessage);
                loadingLiveData.postValue(false);
            }
        });
    }
}