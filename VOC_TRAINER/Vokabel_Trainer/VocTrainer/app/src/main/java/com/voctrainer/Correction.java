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
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class Correction extends AppCompatActivity implements View.OnClickListener{

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

    private ArrayList<Vocabulary> quizList;
    private VocabularyList selVocList;

    private TextView tv_german_word;
    private TextView tv_counterVocs;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btn_continue;
    private Button btn_back;
    private RadioButton radioButtonA;
    private RadioButton radioButtonB;
    private RadioButton radioButtonC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_15_correction);
        radioGroup = findViewById(R.id.radioGroup);

        radioButtonA = (RadioButton) findViewById(R.id.radioButton_a);
        radioButtonB = (RadioButton) findViewById(R.id.radioButton_b);
        radioButtonC= (RadioButton) findViewById(R.id.radioButton_c);

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, 0);
        this.level = getIntent().getIntExtra(SELECTED_LEVEL, 0);
        this.progress = getIntent().getIntExtra(LEVEL_PROGRESS, 0);
        this.quizResult = getIntent().getIntExtra(CURRENT_QUIZ_RESULT, 0);

        this.setTitle("Quiz - Level " + String.valueOf(this.level));

        tv_german_word = (TextView) findViewById(R.id.textView_voc);
        tv_counterVocs = (TextView) findViewById(R.id.textView_counter_questions);

        this.quizList = (ArrayList<Vocabulary>) getIntent().getSerializableExtra("quizList");
        this.selVocList = new VocabularyList(this.quizList);

        btn_back = (Button) findViewById(R.id.button_back);
        btn_back.setText("Korrektur beenden");
        btn_back.setOnClickListener(this);

        btn_continue = (Button) findViewById(R.id.button_next);
        btn_continue.setText("nächste");
        btn_continue.setOnClickListener(this);

        radioButtonA.setEnabled(false);
        radioButtonB.setEnabled(false);
        radioButtonC.setEnabled(false);

        setVocabulary(curVocId);
    }

    private void setVocabulary(int i){
        Vocabulary voc = this.selVocList.getVocabularyById(i);
        showVocabulary(voc.getName(), voc);
    }

    private void showVocabulary(String word_german, Vocabulary voc){
        radioButtonA.setBackgroundColor(Color.TRANSPARENT);
        radioButtonB.setBackgroundColor(Color.TRANSPARENT);
        radioButtonC.setBackgroundColor(Color.TRANSPARENT);
        radioGroup.clearCheck();
        tv_german_word.setText(word_german);
        tv_counterVocs.setText("Frage " + (this.curVocId + 1) + "/" + this.selVocList.getSize());

        // Saves position for later correction
        String[] answers = new String[3];
        answers[voc.getCorrectAnswerPos()] = voc.getName_correct();
        answers[voc.getWrong1AnswerPos()] = voc.getName_wrong1();
        answers[voc.getWrong2AnswerPos()] = voc.getName_wrong2();

        radioButtonA.setText(answers[0]);
        radioButtonB.setText(answers[1]);
        radioButtonC.setText(answers[2]);

        // Shows correct answer
        if(answers[0].equals(voc.getName_correct())){
            radioButtonA.setBackgroundColor(COLOR_CORRECT_GREEN);
            radioButtonA.setChecked(true);
        }
        else if(answers[1].equals(voc.getName_correct())){
            radioButtonB.setBackgroundColor(COLOR_CORRECT_GREEN);
            radioButtonB.setChecked(true);
        }
        else{
            radioButtonC.setBackgroundColor(COLOR_CORRECT_GREEN);
            radioButtonC.setChecked(true);
        }

        // Shows wrong answer if it wasn't correct answered
        if(voc.checkAnswerWasCorrect() == false){
            if(answers[0].equals(voc.getGivenAnswer())){
                radioButtonA.setBackgroundColor(COLOR_CORRECT_RED);
                radioButtonA.setChecked(true);
            }
            else if(answers[1].equals(voc.getGivenAnswer())){
                radioButtonB.setBackgroundColor(COLOR_CORRECT_RED);
                radioButtonB.setChecked(true);
            }
            else{
                radioButtonC.setBackgroundColor(COLOR_CORRECT_RED);
                radioButtonC.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_back) {
            btn_continue.setText("nächste");
            this.curVocId--;
            if(this.curVocId == 0) btn_back.setText("Korrektur beenden");
            if(this.curVocId < 0) goToCongratulation();
            else if(this.curVocId >= 0) setVocabulary(this.curVocId);
        }
        else if(v.getId() == R.id.button_next) {
            btn_back.setText("vorherige");
            this.curVocId++;
            if(this.curVocId < this.selVocList.getSize()) {
                setVocabulary(this.curVocId);
                if ((this.curVocId + 1) == this.selVocList.getSize()) btn_continue.setText("Korrektur beenden");
            }
            else goToCongratulation();
        }
    }

    private void goToCongratulation(){
        Intent intent = new Intent(Correction.this, Congratulation.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        intent.putExtra(CURRENT_QUIZ_RESULT, quizResult);
        intent.putExtra("quizList", this.quizList);
        startActivity(intent);
        this.finish();
    }
    public void onBackPressed(){
        goToCongratulation();
    }
}