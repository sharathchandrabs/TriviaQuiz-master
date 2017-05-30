package com.example.shara.triviaquiz;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by shara on 2/7/2017.
 */

public class GetQuestionsAsync extends AsyncTask<String, Integer, ArrayList<Questions>> {

    public static interface IGetQuestions {
        public void setUpTrivia(ArrayList<Questions> questions);
    }

    IGetQuestions mainActivity;

    public GetQuestionsAsync(IGetQuestions activity) {
        this.mainActivity = activity;
    }

    @Override
    protected void onPostExecute(ArrayList<Questions> questions) {
        mainActivity.setUpTrivia(questions);
    }

    @Override
    protected ArrayList<Questions> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                return QuestionFromJSON.QuestionJSONParser.parseQuestions(sb.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
