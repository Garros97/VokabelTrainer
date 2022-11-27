package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Quiz extends AppCompatActivity implements View.OnClickListener{

    private final int COLOR_CORRECT_GREEN = Color.argb(100,100,255,100);
    private final int COLOR_CORRECT_RED = Color.argb(100,255,50,50);

    private final String SELECTED_AREA = "selectedArea";
    private final String SELECTED_LEVEL = "selectedLevel";
    private final String LEVEL_PROGRESS = "levelProgress";
    private final String CURRENT_QUIZ_RESULT = "currentQuizProgress";

    private int curVocId = 0;
    private int areaID;
    private int level;
    private int progress;
    private int quizResult;
    private VocabularyList selVocList = new VocabularyList();

    private TextView tv_german_word;
    private TextView tv_counterVocs;
    public RadioGroup radioGroup;
    private RadioButton radioButtonSelected;
    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui10_quiz);

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, 0);
        this.level = getIntent().getIntExtra(SELECTED_LEVEL, 0);
        this.progress = getIntent().getIntExtra(LEVEL_PROGRESS, 0);

        tv_german_word = (TextView) findViewById(R.id.textView_voc);
        tv_counterVocs = (TextView) findViewById(R.id.textView_counter_questions);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonA = (RadioButton) findViewById(R.id.radioButton_a);
        radioButtonB = (RadioButton) findViewById(R.id.radioButton_b);
        radioButtonC= (RadioButton) findViewById(R.id.radioButton_c);

        btn_cancel = (Button) findViewById(R.id.button_cancel);
        btn_cancel.setText("Quiz beenden");
        btn_cancel.setOnClickListener(this);

        createVocabularySet();
        setVocabulary(curVocId);
        this.setTitle("Quiz - Level " + String.valueOf(this.level));
    }

    /*
     Loads the vocabularies from the csv files and set a vocabulary list
     */
    private void createVocabularySet(){
        Vocabulary voc;
        InputStream is = null;
        String line = "";

        // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
        if(this.areaID == 0) is = getResources().openRawResource(R.raw.vocbook_physics);
        else if(this.areaID == 1) is = getResources().openRawResource(R.raw.vocbook_economic);
        else if(this.areaID == 2) is = getResources().openRawResource(R.raw.vocbook_se);
        else if(this.areaID == 3) is = getResources().openRawResource(R.raw.vocbook_electrical);
        else if(this.areaID == 4) is = getResources().openRawResource(R.raw.vocbook_sociology);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.ISO_8859_1));
        try{
            while((line = reader.readLine()) != null){
                // Split by ';'
                String[] tokens = line.split(";");
                // Read the data and add it to list, if the level is the right ones
                if(tokens[0].equals(String.valueOf(this.level))){
                    voc = new Vocabulary(tokens[1], tokens[2], tokens[3], tokens[4]);
                    this.selVocList.add(voc);
                }
            }
        }
        catch(IOException e){
            Log.wtf("MyActivity", "Error: Fehler beim Lesen der Datei in Zeiel: " + line, e);
            e.printStackTrace();
        }
        this.selVocList.randomizeOrder();
    }

    private void showVocabulary(String word_german, Vocabulary voc){
        radioButtonA.setBackgroundColor(Color.TRANSPARENT);
        radioButtonB.setBackgroundColor(Color.TRANSPARENT);
        radioButtonC.setBackgroundColor(Color.TRANSPARENT);
        radioGroup.clearCheck();
        tv_german_word.setText(word_german);
        tv_counterVocs.setText("Frage " + (this.curVocId + 1) + "/" + this.selVocList.getSize());

        // Randomize position and save position for later correction
        voc.randomizeOrder();
        // Get random answer position and names
        String[] answers = new String[3];
        answers[voc.getCorrectAnswerPos()] = voc.getName_correct();
        answers[voc.getWrong1AnswerPos()] = voc.getName_wrong1();
        answers[voc.getWrong2AnswerPos()] = voc.getName_wrong2();

        radioButtonA.setText(answers[0]);
        radioButtonB.setText(answers[1]);
        radioButtonC.setText(answers[2]);

        radioButtonA.setEnabled(true);
        radioButtonB.setEnabled(true);
        radioButtonC.setEnabled(true);
    }

    private void setVocabulary(int i){
        Vocabulary voc = this.selVocList.getVocabularyById(i);
        showVocabulary(voc.getName(), voc);
    }

    private void callNextQuestion(String answer, int selectedRadioID){

        // We have to set the given answer by the user in the vocabulary object
        this.selVocList.getVocabularyById(this.curVocId).setGivenAnswer(answer);
        //Show if it was correct or not
        radioButtonSelected = findViewById(selectedRadioID);
        if(this.selVocList.getVocabularyById(this.curVocId).checkAnswerWasCorrect()) radioButtonSelected.setBackgroundColor(COLOR_CORRECT_GREEN);
        else radioButtonSelected.setBackgroundColor(COLOR_CORRECT_RED);

        // Counter count up
        this.curVocId++;

        // Show new Vocabulary if still one exists
        if(this.curVocId < this.selVocList.getSize()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVocabulary(curVocId);
                }
            }, 1000);
        }
        // Exit Quiz and go to result if no further vocabulary exists
        else{
            this.quizResult = getPercentageOfCorrectAnswers(); // Sets the result to global variable
            goToActivityResult();
        }
    }

    public void checkRadioButton(View v){
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButtonSelected = findViewById(radioID);
        radioButtonA.setEnabled(false);
        radioButtonB.setEnabled(false);
        radioButtonC.setEnabled(false);
        callNextQuestion((String) radioButtonSelected.getText(), radioID);
    }

    /*
    Calculates percentage of correct answers from 0% to 100%
    */
    private int getPercentageOfCorrectAnswers(){
        double temp1 = (double) this.selVocList.countCorrectAnswers();
        double temp2 = (double) this.selVocList.getSize();
        return (int) ((temp1 / temp2) * 100);
    }

    private void goToActivityResult(){
        Intent intent = new Intent(Quiz.this, Result.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        intent.putExtra(CURRENT_QUIZ_RESULT, quizResult);

        //ArrayList<Vocabulary> send to the Activity Result
        intent.putExtra("quizList", this.selVocList.getVocList());

        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_cancel) {
            Intent intent = new Intent(Quiz.this, LevelSelection.class);
            intent.putExtra(SELECTED_AREA, areaID);
            startActivity(intent);
            this.finish();
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(Quiz.this, ProgressView.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        startActivity(intent);
        this.finish();
    }
}