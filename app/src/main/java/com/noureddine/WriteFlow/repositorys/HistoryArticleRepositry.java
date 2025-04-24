package com.noureddine.WriteFlow.repositorys;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.noureddine.WriteFlow.database.DatabaseArticle;
import com.noureddine.WriteFlow.database.HistoryArticleDao;
import com.noureddine.WriteFlow.model.HistoryArticle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryArticleRepositry {

    private HistoryArticleDao historyArticleDao;
    private ExecutorService executorService;

    public HistoryArticleRepositry(Application application) {
        DatabaseArticle database = DatabaseArticle.getInstance(application);
        historyArticleDao = database.historyArticleDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public void insertArticle(HistoryArticle historyArticle) {
        executorService.execute(() -> historyArticleDao.insertArticle(historyArticle));
    }

    public void updateArticle(HistoryArticle historyArticle) {
        executorService.execute(() -> historyArticleDao.updateArticle(historyArticle));
    }

    public LiveData<List<HistoryArticle>> getAllArticles(String uid) {
        return historyArticleDao.getAllArticles(uid);
    }

}
