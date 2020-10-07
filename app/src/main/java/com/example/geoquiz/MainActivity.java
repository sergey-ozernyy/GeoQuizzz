package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_TRUE = "trueAnswer";
    private static final String KEY_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_VALUE_OF_CHEAT ="com.example.geoquiz.key_value_of_cheat";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private int valueOfCheat = 0;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;
    private int mAnswerIndex = 0;
    private int mTrueAnswerIndex = 0;
    private boolean mISCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerIndex = savedInstanceState.getInt(KEY_ANSWER,0);
            mTrueAnswerIndex = savedInstanceState.getInt(KEY_TRUE,0);
            mISCheater = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            valueOfCheat = savedInstanceState.getInt(KEY_VALUE_OF_CHEAT, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (mAnswerIndex < mQuestionBank.length){
                    mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                    updateQuestion();
                } else {
                    percentTrue();
                }

            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mISCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue, valueOfCheat);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mAnswerIndex <= mCurrentIndex){
                    checkAnswer(true);
                }
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
            if (mAnswerIndex <= mCurrentIndex){
                checkAnswer(false);
            }
           }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mISCheater = CheatActivity.wasAnswerShown(data);
            valueOfCheat = CheatActivity.wasChiting(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_TRUE, mTrueAnswerIndex);
        savedInstanceState.putInt(KEY_ANSWER, mAnswerIndex);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mISCheater);
        savedInstanceState.putInt(KEY_VALUE_OF_CHEAT, valueOfCheat);
        Log.i(TAG, "onSaveInstanceState" + mTrueAnswerIndex);
        Log.i(TAG, "onSaveInstanceState" + mAnswerIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResIs = 0;
        if (mISCheater){
            messageResIs = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue){
                messageResIs = R.string.correct_toast;
                mTrueAnswerIndex += 1;
            } else {
                messageResIs = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResIs, Toast.LENGTH_SHORT).show();
        mAnswerIndex += 1;
    }

    private void percentTrue(){
        double answerIndex = mAnswerIndex;
        double percent = mTrueAnswerIndex / answerIndex * 100;
        Toast.makeText(this, "У вас " +  percent + "% правильных ответов!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this,  mTrueAnswerIndex + " правильных ответов!", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, mAnswerIndex + " ответов всего", Toast.LENGTH_SHORT).show();
    }

}
