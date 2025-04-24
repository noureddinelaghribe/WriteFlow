package com.noureddine.WriteFlow.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.noureddine.WriteFlow.model.HistoryArticle;
import com.noureddine.WriteFlow.repositorys.HistoryArticleRepositry;

import java.util.List;


public class HistoryArticleViewModel {

    private HistoryArticleRepositry historyArticleRepositry;

    public HistoryArticleViewModel(Application application) {
        super();
        historyArticleRepositry = new HistoryArticleRepositry(application);
    }


    public void insertArticle(HistoryArticle historyArticle) {
        historyArticleRepositry.insertArticle(historyArticle);
    }

    public void updateArticle(HistoryArticle historyArticle) {
        historyArticleRepositry.updateArticle(historyArticle);
    }

    public LiveData<List<HistoryArticle>> getAllUsers(String uid) {
        return historyArticleRepositry.getAllArticles(uid);
    }



}
