package com.example.shara.triviaquiz;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by shara on 2/7/2017.
 */

public class Questions implements Serializable {
    int id;
    String text, image;
    ArrayList<String> choices;
    int answer;
    int selectedChoice = -1;

    public int getId() {
        return id;
    }

    public int getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(int selectedChoice) {
        this.selectedChoice = selectedChoice;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getChoices() {
        return choices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.choices = choices;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", image='" + image + '\'' +
                ", choices=" + choices +
                ", answer='" + answer + '\'' +
                '}';
    }

    public Questions createFromJSON(JSONObject json) throws JSONException, ParseException {
        Questions q = new Questions();
        q.choices = new ArrayList<>();
        q.setId(json.getInt("id"));
        q.setText(json.getString("text"));
        if (!json.has("image")) {
            q.setImage("");
        } else {
            q.setImage(json.getString("image"));
        }
        JSONObject choices = json.getJSONObject("choices");
        q.setAnswer(Integer.parseInt(choices.getString("answer")));
        JSONArray choicesArray = choices.getJSONArray("choice");

        for (int i = 0; i < choicesArray.length(); i++) {
            q.choices.add(choicesArray.getString(i));
            Log.d("demo", "choices");
        }
        return q;
    }
}
