//package com.noureddine.WriteFlow.database;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.room.TypeConverters;
//
//import com.noureddine.WriteFlow.model.HistoryArticle;
//
//@Database(entities = {HistoryArticle.class}, version = 1, exportSchema = false)
//@TypeConverters({GrammarCheckerConverter.class})
//public abstract class DatabaseArticle extends RoomDatabase {
//
//    public abstract HistoryArticleDao historyArticleDao();
//    private static final String DATABASE_NAME = "writeflow_db";
//    private static DatabaseArticle databaseArticle;
//
//    public static synchronized DatabaseArticle getInstance(Context context) {
//        if (databaseArticle == null) {
//            databaseArticle = Room.databaseBuilder(
//                            context.getApplicationContext(),
//                            DatabaseArticle.class,
//                            DATABASE_NAME)
//                    .fallbackToDestructiveMigration()
//                    .build();
//        }
//        return databaseArticle;
//    }
//
//}



package com.noureddine.WriteFlow.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.noureddine.WriteFlow.model.HistoryArticle;

@Database(entities = {HistoryArticle.class}, version = 2, exportSchema = false)
@TypeConverters({GrammarCheckerConverter.class})
public abstract class DatabaseArticle extends RoomDatabase {

    public abstract HistoryArticleDao historyArticleDao();
    private static final String DATABASE_NAME = "writeflow_db";
    private static DatabaseArticle databaseArticle;

    public static synchronized DatabaseArticle getInstance(Context context) {
        if (databaseArticle == null) {
            databaseArticle = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DatabaseArticle.class,
                            DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return databaseArticle;
    }
}
