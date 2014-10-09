package com.example.jhancarlos.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends Activity {
    /*Others Variables*/
    private int mCurrentIndex;
    private int mMark;
    /*Variables savedInstanceState*/
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_MARK = "result";
    private static final String KEY_QUIZ_STATICS = "quiz_statics";
    /*Buttons*/
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mResetButton;
    private Button mGoBackButton;
    /*private Button mNextButton;
    private Button mPrevButton;*/
    /*TextViews*/
    private TextView mQuestionTextView;
    private TextView mQuestionNumber;
    private TextView mQuizResultTextView;
    /*Variables Questionary*/
    private TrueFalse[] mQuestionBank;
    private int quiz;
    private boolean[] mQuizResultBank;
    /*Sonidos*/
    private MediaPlayer correct;
    private MediaPlayer failure;

    //Funcion que actualiza el Cuestionario y indica si has aprobado o suspendido el cuestionario
    private void updateQuestion() {

        if (mCurrentIndex >= mQuestionBank.length) {
            double porcentaje = ((double) mMark / (double) mQuestionBank.length) * 100;

            mQuestionTextView.setText("Porcentaje de aciertos: " + (int) porcentaje + "%");

            if ((int) porcentaje > 63) {
                mQuestionNumber.setText("Aprobado");
                mQuestionNumber.setTextColor(Color.parseColor("#01DF01"));
                MediaPlayer win;
                win = MediaPlayer.create(this,R.raw.quiz_win);
                win.start();

            } else {
                mQuestionNumber.setText("Suspendido");
                mQuestionNumber.setTextColor(Color.parseColor("#FF0000"));
                MediaPlayer lose;
                lose = MediaPlayer.create(this,R.raw.quiz_failure);
                lose.start();
            }

            mResetButton.setVisibility(View.VISIBLE);
            //mGoBackButton.setVisibility(View.VISIBLE);
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);

            String quiz_statics = "Resultados cuestionario:\n\n";

            int aux = 1;

            for (boolean results : mQuizResultBank) {
                if (results == true) {
                    quiz_statics = quiz_statics + "Respuesta - " + aux + ": Correcto\n";
                } else {
                    quiz_statics = quiz_statics + "Respuesta - " + aux + ": Incorrecto\n";
                }

                aux++;
            }

            mQuizResultTextView.setText(quiz_statics);

        } else {
            int question = mQuestionBank[mCurrentIndex].getQuestion();
            mQuestionTextView.setText(question);
            mQuestionNumber.setText("Pregunta: " + (mCurrentIndex+1) +"/" + mQuestionBank.length);

        }

    }

    //Funcion que comprueba si has contestado bien una pregunta
    private void checkAnswer(boolean userPressesTrue) {

        if(mCurrentIndex>=mQuestionBank.length){
            return;
        }

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();
        int messageResId = 0;
        Toast anserToast = new Toast(this);
        LayoutInflater inflater = getLayoutInflater();
        View layouttrue = inflater.inflate(R.layout.toast_layout_true, (ViewGroup)findViewById(R.id.lytLayoutTrue));
        View layoutfalse = inflater.inflate(R.layout.toast_layout_false, (ViewGroup)findViewById(R.id.lytLayoutFalse));
        TextView textMsgTrue = (TextView) layouttrue.findViewById(R.id.txtMensajeTrue);
        TextView textMsgFalse = (TextView) layoutfalse.findViewById(R.id.txtMensajeFalse);




        if (userPressesTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mMark++;
            mQuizResultBank[mCurrentIndex]=true;

            textMsgTrue.setText(messageResId);
            anserToast.setDuration(Toast.LENGTH_SHORT);
            anserToast.setView(layouttrue);
            anserToast.show();
            correct = MediaPlayer.create(this,R.raw.question_correct);
            correct.start();

        } else {
            messageResId = R.string.incorrect_toast;
            mQuizResultBank[mCurrentIndex] = false;
            textMsgFalse.setText(messageResId);
            anserToast.setDuration(Toast.LENGTH_SHORT);
            anserToast.setView(layoutfalse);
            anserToast.show();
            failure = MediaPlayer.create(this,R.raw.question_failure);
            failure.start();
        }


    }

    //Funcion que reinicia el cuestionario
    private void resetQuiz(){
        mMark=0;
        mCurrentIndex=0;
        updateQuestion();
        mResetButton.setVisibility(View.INVISIBLE);
       // mGoBackButton.setVisibility(View.INVISIBLE);
        mQuizResultTextView.setText("");
        mQuestionNumber.setTextColor(Color.parseColor("#FFFFFF"));
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getActionBar().hide();

        //Inicializamos el cuestionario obteniendo el valor del cuestionario
        //desde el intent
        quiz = getIntent().getIntExtra("quiz",0);
        Log.i(TAG, String.valueOf(quiz));
        mQuestionBank =  getQuestionBank(quiz);
        mQuizResultBank = new boolean[mQuestionBank.length];

        //accedemos al textview que muestra el numero de pregunta
        mQuestionNumber = (TextView) findViewById(R.id.question_number);

        //Accedemos al textview que muestra los resultados del cuestionario
        mQuizResultTextView = (TextView) findViewById(R.id.statics);

        //Accedemos al boton reset y añadimos el evento on clic para el reinicio
        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                resetQuiz();
            }
        });
        //accedemos al boton de regrezar a la lista de cuestionarios
        mGoBackButton = (Button) findViewById(R.id.goback_button);

        mGoBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Accedemos al textview contiene la pregunta
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        //Accedemos al boton true y añadimos el evento onclick
        mTrueButton = (Button) findViewById(R.id.true_button);

        mTrueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    checkAnswer(true);
                    mCurrentIndex++;
                    updateQuestion();
            }
        });
        //Accedemos al boton flase y añadimos el evento onclick
        mFalseButton = (Button) findViewById(R.id.false_button);

        mFalseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                checkAnswer(false);
                mCurrentIndex++;
                updateQuestion();
            }
        });
        //guardamos el estado de la instancia para que al cambiar a la vista land no se pierdan los datos
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mMark = savedInstanceState.getInt(KEY_MARK,0);
            mQuizResultBank = savedInstanceState.getBooleanArray(KEY_QUIZ_STATICS);
        }

        updateQuestion();
    }
    //Reescribimos el metodo onSaveIntanceState
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(KEY_MARK, mMark);
        savedInstanceState.putBooleanArray(KEY_QUIZ_STATICS, mQuizResultBank);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Futura implementacionde QuizBank MAP.
    /*
    private void addQuizs(){
        String keyQuiz = "Geografia";
        TrueFalse[] quiz = new TrueFalse[]{
                new TrueFalse(R.string.question_oceans, true),
                new TrueFalse(R.string.question_mideast, false),
                new TrueFalse(R.string.question_africa, false),
                new TrueFalse(R.string.question_americas, true),
                new TrueFalse(R.string.question_asia, true)
        };
        QuizBank.addQuiz(keyQuiz,quiz);
        keyQuiz = "Android";
         quiz = new TrueFalse[]{
                 new TrueFalse(R.string.android1, true),
                 new TrueFalse(R.string.android2, true),
                 new TrueFalse(R.string.android3, false),
                 new TrueFalse(R.string.android4, false),
                 new TrueFalse(R.string.android5, true)
        };
        QuizBank.addQuiz(keyQuiz,quiz);

    }*/

        //Banco de cuestionarios
    private TrueFalse[] getQuestionBank(int quiznum) {

        switch (quiznum) {

            default:
                return new TrueFalse[] {
                        new TrueFalse(R.string.question_oceans, true),
                        new TrueFalse(R.string.question_mideast, false),
                        new TrueFalse(R.string.question_africa, false),
                        new TrueFalse(R.string.question_americas, true),
                        new TrueFalse(R.string.question_asia, true)};
            case 0:
                return new TrueFalse[] {
                        new TrueFalse(R.string.question_oceans, true),
                        new TrueFalse(R.string.question_mideast, false),
                        new TrueFalse(R.string.question_africa, false),
                        new TrueFalse(R.string.question_americas, true),
                        new TrueFalse(R.string.question_asia, true) };

            case 1:
                return new TrueFalse[] {
                        new TrueFalse(R.string.android1, true),
                        new TrueFalse(R.string.android2, true),
                        new TrueFalse(R.string.android3, false),
                        new TrueFalse(R.string.android4, false),
                        new TrueFalse(R.string.android5, true) };

            case 2:
                return new TrueFalse[] {
                        new TrueFalse(R.string.spring1, true),
                        new TrueFalse(R.string.spring2, false),
                        new TrueFalse(R.string.spring3, true),
                        new TrueFalse(R.string.spring4, false),
                        new TrueFalse(R.string.spring5, true) };

            case 3:
                return new TrueFalse[] {
                        new TrueFalse(R.string.programation1, true),
                        new TrueFalse(R.string.programation2, true),
                        new TrueFalse(R.string.programation3, true),
                        new TrueFalse(R.string.programation4, true),
                        new TrueFalse(R.string.programation5, false) };
            case 4:
                return new TrueFalse[] {
                        new TrueFalse(R.string.stucom1, true),
                        new TrueFalse(R.string.stucom2, true),
                        new TrueFalse(R.string.stucom3, true),
                        new TrueFalse(R.string.stucom4, true),
                        new TrueFalse(R.string.stucom5, true) };
        }
    }
}
