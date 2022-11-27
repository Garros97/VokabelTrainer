package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProgressView extends AppCompatActivity implements View.OnClickListener {

    private final String SELECTED_AREA = "selectedArea";
    private final String SELECTED_LEVEL = "selectedLevel";
    private final String LEVEL_PROGRESS = "levelProgress";

    private int areaID;
    private int level;
    private int progress;

    private ProgressBar progressBar;
    private TextView progressText;
    private Button btn_startQuiz;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui9_progress_view);
        this.setTitle("Fortschritt");

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, 0);
        this.level = getIntent().getIntExtra(SELECTED_LEVEL, 0);
        this.progress = getIntent().getIntExtra(LEVEL_PROGRESS, 0);

        btn_startQuiz = (Button) findViewById(R.id.button_start_quiz);
        btn_startQuiz.setText("Starte Quiz");
        btn_startQuiz.setOnClickListener(this);

        btn_back = (Button) findViewById(R.id.button_back);
        btn_back.setText("anderen level wählen");
        btn_back.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        progressText = (TextView) findViewById(R.id.progressText_detail);

        progressText.setText(String.valueOf(this.progress + " %"));
        progressBar.setProgress(this.progress);
    }
    private void startQuiz(){
        Intent intent = new Intent(ProgressView.this, Quiz.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_start_quiz) {
            btn_startQuiz.setVisibility(View.INVISIBLE);
            btn_back.setVisibility(View.INVISIBLE);

            // Message to user to explain that has directly select an answer if clicked on a Radio Button
            Toast.makeText(getApplicationContext(),"Hinweis: Durch drücken einer Antwort wird diese sofort akzeptiert.\n\nQuiz wird gestartet...", Toast.LENGTH_LONG).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startQuiz();
                }
            }, 2500);
        }
        else if(v.getId() == R.id.button_back) {
            Intent intent = new Intent(ProgressView.this, LevelSelection.class);
            intent.putExtra(SELECTED_AREA, areaID);
            startActivity(intent);
            this.finish();
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(ProgressView.this, LevelSelection.class);
        intent.putExtra(SELECTED_AREA, areaID);
        startActivity(intent);
        this.finish();
    }
}