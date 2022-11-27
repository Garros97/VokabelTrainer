package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;


public class Congratulation extends AppCompatActivity implements View.OnClickListener{

    private final String SELECTED_AREA = "selectedArea";
    private final String SELECTED_LEVEL = "selectedLevel";
    private final String LEVEL_PROGRESS = "levelProgress";
    private final String CURRENT_QUIZ_RESULT = "currentQuizProgress";
    private final int LEVEL_UP = 70; // Level is reached of a progress quote of 70%

    private int areaID;
    private int level;
    private int progress;
    private int quizResult;
    private ArrayList<Vocabulary> quizList;

    private Button btn_exit;
    private Button btn_correction;
    private Button btn_repeatQuiz;
    private TextView tvRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui14_congratulation);
        this.setTitle("Quiz beenden");

        btn_exit = (Button) findViewById(R.id.button_exit);
        btn_exit.setText("Quiz beenden");
        btn_exit.setOnClickListener(this);

        btn_repeatQuiz = (Button) findViewById(R.id.button_repeatQuiz);
        btn_repeatQuiz.setText("Quiz wiederholen");
        btn_repeatQuiz.setOnClickListener(this);

        btn_correction = (Button) findViewById(R.id.button_correction);
        btn_correction.setText("Korrektur");
        btn_correction.setOnClickListener(this);

        tvRes = (TextView) findViewById(R.id.tvResult);

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, 0);
        this.level = getIntent().getIntExtra(SELECTED_LEVEL, 0);
        this.progress = getIntent().getIntExtra(LEVEL_PROGRESS, 0);
        this.quizResult = getIntent().getIntExtra(CURRENT_QUIZ_RESULT, 0);

        this.quizList = (ArrayList<Vocabulary>) getIntent().getSerializableExtra("quizList");

        showMessage();
    }

    private void showMessage(){

        // Last Progress >= 70% && Current Progress >= 70%
        if((this.progress >= LEVEL_UP) && (this.quizResult >= LEVEL_UP)){
            // Current Progress > Last Progress
            if(this.quizResult > this.progress) tvRes.setText("Gratulation!\n" + "Du hast hast dich verbessert!\nWeiter so! :)");
            // Current Progress <= Last Progress
            else tvRes.setText("Quiz erfolgreich absolviert.\n" + "Du hast dich aber nicht verbessert.");
        }
        // Last Progress < 70% && Current Progress >= 70%
        else if((this.progress < LEVEL_UP) && (this.quizResult >= LEVEL_UP)){
            int newLevel = this.level + 1;
            if(newLevel == 4) tvRes.setText("Gratulation!\n" + "Du hast Level 3 geschafft und diesen Fachbereich abgeschlossen!");
            else tvRes.setText("Gratulation!\n" + "Du hast Level " + newLevel + " erreicht!");
        }
        // Last Progress >= 70% && Current Progress < 70%
        else if((this.progress >= LEVEL_UP) && (this.quizResult < LEVEL_UP)){
            tvRes.setText("Du hast wohl einige Vokabeln vergessen." + "\n\nDu bleibst auf Level " + this.level
                    + "\n\n Versuche das Quiz später erneut!");
        }
        // Last Progress < 70% && Current Progress < 70%
        else {
            tvRes.setText("Du bleibst auf Level " + this.level
                    + "\n\n Versuche das Quiz später erneut!");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_exit) {
            Intent intent = new Intent(Congratulation.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        else if(v.getId() == R.id.button_correction) {
            Intent intent = new Intent(Congratulation.this, Correction.class);
            intent.putExtra(SELECTED_AREA, areaID);
            intent.putExtra(SELECTED_LEVEL, level);
            intent.putExtra(LEVEL_PROGRESS, progress);
            intent.putExtra(CURRENT_QUIZ_RESULT, quizResult);
            intent.putExtra("quizList", this.quizList);
            startActivity(intent);
            this.finish();
        }
        else if(v.getId() == R.id.button_repeatQuiz) goBackToQuiz();
    }

    private void goBackToQuiz(){
        Intent intent = new Intent(Congratulation.this, Quiz.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        startActivity(intent);
        this.finish();
    }

    public void onBackPressed(){
        goBackToQuiz();
    }
}