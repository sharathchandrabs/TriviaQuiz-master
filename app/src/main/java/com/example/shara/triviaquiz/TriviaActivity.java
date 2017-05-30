package com.example.shara.triviaquiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TriviaActivity extends AppCompatActivity {
    ArrayList<Questions> displayQuestions;
    TextView timerTextView;
    TextView questionNumberTextView;
    TextView questionTextView;
    ImageView questionImageView;
    RadioGroup radioGroup;
    RelativeLayout imageRelativeLayout;
    ProgressBar progressBar;
    TextView loadImageTextView;
    //    RadioButton radioButtonChoices;
    LinearLayout checkBoxLinearLyout;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        timerTextView = (TextView) findViewById(R.id.timeCounterTextView);
        Bundle receivedFromMainQuestions = getIntent().getExtras();
        if (receivedFromMainQuestions != null) {
            count = 0;
            displayQuestions = (ArrayList<Questions>) receivedFromMainQuestions.getSerializable(getResources().getString(R.string.mainIntentStartTriviaStringConstant));
            radioGroup = (RadioGroup) findViewById(R.id.radioGroupForChoices);
            questionNumberTextView = (TextView) findViewById(R.id.questionNumberTextView);
            questionTextView = (TextView) findViewById(R.id.questionTextView);
            loadImageTextView = (TextView) findViewById(R.id.txt_ProgressDialogMessage);
            questionImageView = (ImageView) findViewById(R.id.questionImageView);
            imageRelativeLayout = (RelativeLayout) findViewById(R.id.imageViewRelativeLayout);
            progressBar = (ProgressBar) findViewById(R.id.progressBar2);
            final Button previousButton = (Button) findViewById(R.id.previousButton);
            final Button nextButton = (Button) findViewById(R.id.nextButton);
//            previousButton.setVisibility(View.INVISIBLE);
            previousButton.setEnabled(false);
            createQuestionView(displayQuestions.get(count));
            final CountDownTimer timer = new CountDownTimer(120000, 1000) {

                public void onTick(long millisUntilFinished) {
                    timerTextView.setText(getResources().getString(R.string.countdownTimer) + Long.toString((millisUntilFinished / 1000)) + getResources().getString(R.string.seconds));
                }

                public void onFinish() {

                    Intent statsIntent = new Intent(TriviaActivity.this, StatsActivity.class);
                    statsIntent.putExtra(getResources().getString(R.string.SendToStats), TriviaActivity.this.displayQuestions);
                    startActivity(statsIntent);
                    finish();
                }
            };
            timer.start();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    displayQuestions.get(count).setSelectedChoice(group.indexOfChild(findViewById(checkedId)));
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (--count == 0)
                        previousButton.setEnabled(false);
                    if (count != displayQuestions.size() - 1)
                        nextButton.setText(getResources().getString(R.string.nextButton));
                    createQuestionView(displayQuestions.get(count));
                    if (displayQuestions.get(count).getSelectedChoice() != -1)
                        radioGroup.check(((RadioButton) radioGroup.getChildAt(displayQuestions.get(count).getSelectedChoice())).getId());
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (++count < displayQuestions.size()) {
                        if (count == displayQuestions.size() - 1)
                            nextButton.setText(getResources().getString(R.string.FinishButtonText));
//                        previousButton.setVisibility(View.VISIBLE);
                        previousButton.setEnabled(true);
                        createQuestionView(displayQuestions.get(count));
                        if (displayQuestions.get(count).getSelectedChoice() != -1)
                            radioGroup.check(((RadioButton) radioGroup.getChildAt(displayQuestions.get(count).getSelectedChoice())).getId());
                    } else {
                        Intent statsIntent = new Intent(TriviaActivity.this, StatsActivity.class);
                        statsIntent.putExtra(getResources().getString(R.string.SendToStats), TriviaActivity.this.displayQuestions);
                        timer.cancel();
                        startActivity(statsIntent);
                        finish();
                    }
                }
            });

        } else {
            Toast.makeText(this, getResources().getString(R.string.NoQuestionReceived), Toast.LENGTH_LONG).show();
        }
    }

    public void createQuestionView(Questions q) {
        questionNumberTextView.setText(getResources().getString(R.string.questionSymbol) + String.valueOf(q.getId() + 1));
        questionTextView.setText(q.getText());
        radioGroup.removeAllViews();
        questionImageView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        loadImageTextView.setVisibility(View.VISIBLE);

        if (!q.getImage().equals("")) {
            Picasso.with(this)
                    .load(q.getImage())
//                .placeholder(R.layout.progressdialoglayout)
                    .error(R.drawable.image)
                    .into(questionImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.INVISIBLE);
                            questionImageView.setVisibility(View.VISIBLE);
                            loadImageTextView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            // Show some error message
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    });
        } else {

            Picasso.with(this)
                    .load(R.drawable.image)
                    .placeholder(R.drawable.trivia)
                    .error(R.drawable.image)
                    .into(questionImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.INVISIBLE);
                            questionImageView.setVisibility(View.VISIBLE);
                            loadImageTextView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            // Show some error message
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    });
        }
        for (int i = 0; i < q.choices.size(); i++) {
            RadioButton radioButtonChoices = new RadioButton(TriviaActivity.this);
            radioButtonChoices.setText(q.getChoices().get(i));
            radioButtonChoices.setClickable(true);
            radioButtonChoices.setEnabled(true);
            radioGroup.addView(radioButtonChoices);
        }
    }
}
