//package com.noureddine.phraseflow.viewModels;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.noureddine.phraseflow.model.DeepSeekResponse;
//import com.noureddine.phraseflow.model.TypeProcessing;
//import com.noureddine.phraseflow.repositorys.DeepSeekRepository;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class MainViewModel extends ViewModel {
//    private final DeepSeekRepository repository;
//    private static final String API_KEY = "sk-03701bb8875a403090a9a7cf5a737d25";
//    private final MutableLiveData<String> responseText = new MutableLiveData<>();
//    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
//    private final MutableLiveData<String> error = new MutableLiveData<>();
//
//    public MainViewModel() {
//        repository = new DeepSeekRepository();
//    }
//
//    public LiveData<String> getResponseText() {
//        return responseText;
//    }
//
//    public LiveData<Boolean> getIsLoading() {
//        return isLoading;
//    }
//
//    public LiveData<String> getError() {
//        return error;
//    }
//
//    public void sendPrompt(TypeProcessing typeProcessing) {
//        isLoading.setValue(true);
//
//        repository.getChatCompletion(typeProcessing, API_KEY).enqueue(new Callback<DeepSeekResponse>() {
//            @Override
//            public void onResponse(Call<DeepSeekResponse> call, Response<DeepSeekResponse> response) {
//                isLoading.setValue(false);
//
//                if (response.isSuccessful() && response.body() != null) {
//                    DeepSeekResponse responseBody = response.body();
//                    if (responseBody.getChoices() != null && !responseBody.getChoices().isEmpty()) {
//                        responseText.setValue(responseBody.getChoices().get(0).getMessage().getContent()+"\n"+responseBody.getUsage().getTotal_tokens());
//                    } else {
//                        error.setValue("لم يتم استلام إجابة من الخادم");
//                    }
//                } else {
//                    error.setValue("خطأ: " + response.code() + " - " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DeepSeekResponse> call, Throwable t) {
//                isLoading.setValue(false);
//                error.setValue("حدث خطأ: " + (t.getMessage() != null ? t.getMessage() : "خطأ غير معروف"));
//            }
//        });
//    }
//}
