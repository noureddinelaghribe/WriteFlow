package com.noureddine.WriteFlow.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.noureddine.WriteFlow.model.HistoryArticle;

import java.util.List;

@Dao
public interface HistoryArticleDao {

    @Insert
    void insertArticle(HistoryArticle historyArticle);

    @Update
    void updateArticle(HistoryArticle historyArticle);

    @Query("SELECT * FROM HistoryArticle  WHERE uid=:uid")
    LiveData<List<HistoryArticle>> getAllArticles(String uid);

}
