package com.example.shara.triviaquiz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetQuestionsAsync.IGetQuestions {
    ArrayList<Questions> finalQuestions;
    ImageView triviaLogo;
    ProgressDialog progress;
    Button startTrivia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check connection
        if(isConnectedOnline()){
            triviaLogo = (ImageView) findViewById(R.id.triviaLogoImageView);
            triviaLogo.setVisibility(View.INVISIBLE);
            progress = new ProgressDialog(this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage(getResources().getString(R.string.loadingTrivia));
            String urlToGet = getString(R.string.urlToGetQuestions);
            new GetQuestionsAsync(this).execute(urlToGet);
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.noConnection), Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.exitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setUpTrivia(final ArrayList<Questions> questions) {
        Log.d("demo", questions.get(questions.size() - 1).toString());
        triviaLogo.setVisibility(View.VISIBLE);
        startTrivia = (Button) findViewById(R.id.startTriviaButton);
        startTrivia.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_dark));
        findViewById(R.id.startTriviaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent triviaActivity = new Intent(MainActivity.this, TriviaActivity.class);
                triviaActivity.putExtra(getString(R.string.mainIntentStartTriviaStringConstant), questions);
                startActivity(triviaActivity);
            }
        });
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
