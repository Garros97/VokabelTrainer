package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import androidx.appcompat.app.AppCompatActivity;

/*

 CSV File Format:

 [level];[name];[correct];[wrong1][wrong2]

 Example dataset:
 2;Auto;car;house;tree

 */

public class VocabularyView extends AppCompatActivity implements View.OnClickListener {

    private final String SELECTED_AREA = "selectedArea";
    private final String SELECTED_LEVEL = "selectedLevel";
    private final String LEVEL_PROGRESS = "levelProgress";

    private int curVocId = 0;
    private VocabularyList selVocList = new VocabularyList();

    private int areaID;
    private int level;
    private int progress;

    private Button btn_back;
    private Button btn_next;

    private TextView tv_ger;
    private TextView tv_eng;
    private TextView tv_counterVocs;
    private ImageView ivIconArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui6_vocabulary_view);

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, 0);
        this.level = getIntent().getIntExtra(SELECTED_LEVEL, 0);
        this.progress = getIntent().getIntExtra(LEVEL_PROGRESS, 0);

        btn_back = (Button) findViewById(R.id.button_back);
        btn_back.setText("zurück");
        btn_back.setOnClickListener(this);

        btn_next = (Button) findViewById(R.id.button_next);
        btn_next.setText("nächste");
        btn_next.setOnClickListener(this);

        tv_ger = (TextView) findViewById(R.id.tv_german_word);
        tv_eng = (TextView) findViewById(R.id.tv_english_word);
        ivIconArea = (ImageView) findViewById(R.id.ivPictureArea);

        tv_counterVocs = (TextView) findViewById(R.id.textView_counter_vocs);

        setAreaIcon();
        createVocabularySet();
        setVocabulary(this.curVocId);

        this.setTitle("Vokabeln lernen - Level " + String.valueOf(this.level));
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
            Log.wtf("VocabularyView", "Error reading data file on line" + line, e);
            e.printStackTrace();
        }
        this.selVocList.randomizeOrder();
    }

    private void showVocabulary(String word_german, String word_english){
        tv_ger.setText(word_german);
        tv_eng.setText(word_english);
        tv_counterVocs.setText("Vokabel " + (this.curVocId + 1) + "/" + this.selVocList.getSize());
    }

    private void setVocabulary(int i){
        Vocabulary voc = this.selVocList.getVocabularyById(i);
        showVocabulary(voc.getName(), voc.getName_correct());
    }

    private void setAreaIcon(){
        // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
        if(this.areaID == 0) ivIconArea.setImageResource(R.drawable.image_area_physics);
        else if(this.areaID == 1) ivIconArea.setImageResource(R.drawable.image_area_economic);
        else if(this.areaID == 2) ivIconArea.setImageResource(R.drawable.image_area_se);
        else if(this.areaID == 3) ivIconArea.setImageResource(R.drawable.image_area_electrical);
        else if(this.areaID == 4) ivIconArea.setImageResource(R.drawable.image_area_sociology);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_back) {
            btn_next.setText("nächste");
            this.curVocId--;
            if(this.curVocId < 0){
                Intent intent = new Intent(VocabularyView.this, LevelSelection.class);
                intent.putExtra(SELECTED_AREA, areaID);
                intent.putExtra(SELECTED_LEVEL, level);
                intent.putExtra(LEVEL_PROGRESS, progress);
                startActivity(intent);
                this.finish();
            }
            else if(this.curVocId >= 0) setVocabulary(this.curVocId);
        }
        else if(v.getId() == R.id.button_next) {
            this.curVocId++;
            if(this.curVocId < this.selVocList.getSize()) {
                setVocabulary(this.curVocId);
                if((this.curVocId + 1) == this.selVocList.getSize()) btn_next.setText("bereit");
            }
            else{
                Intent intent = new Intent(VocabularyView.this, ProgressView.class);
                intent.putExtra(SELECTED_AREA, areaID);
                intent.putExtra(SELECTED_LEVEL, level);
                intent.putExtra(LEVEL_PROGRESS, progress);
                startActivity(intent);
                this.finish();
            }
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(VocabularyView.this, LevelSelection.class);
        intent.putExtra(SELECTED_AREA, areaID);
        intent.putExtra(SELECTED_LEVEL, level);
        intent.putExtra(LEVEL_PROGRESS, progress);
        startActivity(intent);
        this.finish();
    }
}