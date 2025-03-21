package com.noureddine.WriteFlow.database;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.noureddine.WriteFlow.model.GrammarChecker;

public class GrammarCheckerConverter {

    @TypeConverter
    public static GrammarChecker fromString(String value) {
        return value == null ? null : new Gson().fromJson(value, GrammarChecker.class);
    }

    @TypeConverter
    public static String grammarCheckerToString(GrammarChecker grammarChecker) {
        return grammarChecker == null ? null : new Gson().toJson(grammarChecker);
    }
}

