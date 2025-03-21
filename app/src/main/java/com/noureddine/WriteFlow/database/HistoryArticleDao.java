package com.noureddine.WriteFlow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.noureddine.WriteFlow.model.HistoryArticle;

import java.util.List;

@Dao
public interface HistoryArticleDao {

    @Insert
    Long insertArticle(HistoryArticle historyArticle);

    @Query("SELECT * FROM HistoryArticle  WHERE uid=:uid")
    LiveData<List<HistoryArticle>> getAllArticles(String uid);

}
