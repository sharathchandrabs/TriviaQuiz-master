package com.example.shara.triviaquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    ArrayList<Questions> answers;
    LinearLayout statsLinear;
    TextView question;
    TextView userAnswer;
    TextView correctAnswer;
    double correctAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        TextView resultPercentage = (TextView)findViewById(R.id.resultPercentageTextView);

        Bundle receivedFromTrivia = getIntent().getExtras();
        if (receivedFromTrivia != null) {
            answers = (ArrayList<Questions>) receivedFromTrivia.getSerializable(getResources().getString(R.string.SendToStats));
            statsLinear = (LinearLayout) findViewById(R.id.correctAnswersDisplayLinearLayout);

            for (Questions ans : answers) {
                question = new TextView(this);
                correctAnswer = new TextView(this);
                userAnswer = new TextView(this);
                question.setText(getResources().getString(R.string.question)+ans.getText());
                if(ans.getSelectedChoice()!= -1){
                    userAnswer.setText(getResources().getString(R.string.userAnswer)+ans.getChoices().get(ans.getSelectedChoice()));
                }
                else {
                    userAnswer.setText(getResources().getString(R.string.userAnswer)+getResources().getString(R.string.choicenotselected));
                }

                correctAnswer.setText(getResources().getString(R.string.answer)+ans.getChoices().get(ans.getAnswer()-1));
                userAnswer.setBackgroundResource(android.R.color.holo_red_dark);
                statsLinear.addView(question);
                statsLinear.addView(userAnswer);
                statsLinear.addView(correctAnswer);
                if (ans.getSelectedChoice() == (ans.getAnswer()-1)) {
                    correctAnswersCount++;
                }
            }

//            correctAnswersCount = ;
            progressBar.setProgress((int)Math.round((correctAnswersCount / (double)answers.size()) * 100));
            resultPercentage.setText(Double.toString(Math.round((correctAnswersCount / (double)answers.size()) * 100)) + getResources().getString(R.string.resultPercentage));

        } else
            Toast.makeText(this, getResources().getString(R.string.invalid), Toast.LENGTH_LONG).show();

        findViewById(R.id.finishButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
