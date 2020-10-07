package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.geoquiz.answer_is_true";

    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.geoquiz.answer_shown";

    private static final String EXTRA_VALUE_OF_CHEAT =
            "com.example.geoquiz.extra_value_of_cheat";

    private static final String KEY_ANSWER_SHOWN =
            "com.example.geoquiz.key_answer_shown";

    private static final String KEY_VALUE_OF_CHEAT =
            "com.example.geoquiz.key_value_of_cheat";



    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private int valueOfCheat = 0;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int valueOf_Cheat){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(EXTRA_VALUE_OF_CHEAT, valueOf_Cheat);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int wasChiting(Intent result){
        return result.getIntExtra(EXTRA_VALUE_OF_CHEAT, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null){
            mAnswerIsTrue = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
            valueOfCheat = savedInstanceState.getInt(KEY_VALUE_OF_CHEAT, 0);
            setAnswerShowResult(mAnswerIsTrue);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        valueOfCheat = getIntent().getIntExtra(EXTRA_VALUE_OF_CHEAT,0);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                valueOfCheat += 1;
                int toastId = 0;
                if (valueOfCheat <= 3){
                    if (mAnswerIsTrue){
                        mAnswerTextView.setText(R.string.true_button);
                    } else {
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    setAnswerShowResult(true);
                    if (valueOfCheat == 1){
                        toastId = R.string.one_toast;
                    }
                    if (valueOfCheat == 2) {
                        toastId = R.string.two_toast;
                    }
                    if (valueOfCheat == 3) {
                            toastId = R.string.three_toast;
                    }
                } else {
                    toastId = R.string.out_toast;
                    mShowAnswerButton.setEnabled(false);
                }
                Toast.makeText(CheatActivity.this, toastId, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerIsTrue);
        savedInstanceState.putInt(KEY_VALUE_OF_CHEAT, valueOfCheat);
        }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setAnswerShowResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(EXTRA_VALUE_OF_CHEAT, valueOfCheat);
        setResult(RESULT_OK, data);
    }
}
