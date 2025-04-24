package com.noureddine.WriteFlow.Utils;

import com.noureddine.WriteFlow.model.GrammarChecker;

import org.json.JSONException;
import org.json.JSONObject;

public class GsonToGrammarChecker {

    public static GrammarChecker parseGrammarCheckerResponse(String response) {
        try {
            JSONObject jsonContent = new JSONObject(response);
            String text = jsonContent.getString("text");         // Extract the "text" field
            String issue = String.valueOf(jsonContent.getInt("issue")); // Extract and convert the "issue" field to String
            return new GrammarChecker(text, issue);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
