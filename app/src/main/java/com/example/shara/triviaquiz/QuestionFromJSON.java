package com.example.shara.triviaquiz;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by shara on 2/7/2017.
 */

public class QuestionFromJSON {
    static public class QuestionJSONParser{
        static ArrayList<Questions> parseQuestions(String in) throws JSONException, ParseException {
            ArrayList<Questions> questionsList = new ArrayList<>();
            JSONObject root = new JSONObject(in);
            JSONArray questionsJSONArray=root.getJSONArray("questions");

            for (int i = 0; i < questionsJSONArray.length() ; i++) {
                JSONObject questionsContent = questionsJSONArray.getJSONObject(i);
                Questions q = new Questions();
                q = q.createFromJSON(questionsContent);
                questionsList.add(q);
            }

            return questionsList;
        }
    }
}
