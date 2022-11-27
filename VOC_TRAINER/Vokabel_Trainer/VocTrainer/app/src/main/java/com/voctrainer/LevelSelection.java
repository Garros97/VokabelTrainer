package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import static com.voctrainer.R.drawable.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

    /*
     Loads User data and select level
    */
public class LevelSelection extends AppCompatActivity implements View.OnClickListener{

    // Identification of areas (Fachbereiche/Fachgebiete)
    private final String SELECTED_AREA = "selectedArea";
    private final String SELECTED_LEVEL = "selectedLevel";
    private final String LEVEL_PROGRESS = "levelProgress";
    private final int LEVEL_UP = 70; // Level is reached of a progress quote of 70%

    /*
     User data Keys
    */
    private final String USER_DATA_FILE_NAME = "userProgressData";
    private final String USER_DATA_FG0_LVL1 = "userDataKeyFg0L1";
    private final String USER_DATA_FG0_LVL2 = "userDataKeyFg0L2";
    private final String USER_DATA_FG0_LVL3 = "userDataKeyFg0L3";

    private final String USER_DATA_FG1_LVL1 = "userDataKeyFg1L1";
    private final String USER_DATA_FG1_LVL2 = "userDataKeyFg1L2";
    private final String USER_DATA_FG1_LVL3 = "userDataKeyFg1L3";

    private final String USER_DATA_FG2_LVL1 = "userDataKeyFg2L1";
    private final String USER_DATA_FG2_LVL2 = "userDataKeyFg2L2";
    private final String USER_DATA_FG2_LVL3 = "userDataKeyFg2L3";

    private final String USER_DATA_FG3_LVL1 = "userDataKeyFg3L1";
    private final String USER_DATA_FG3_LVL2 = "userDataKeyFg3L2";
    private final String USER_DATA_FG3_LVL3 = "userDataKeyFg3L3";

    private final String USER_DATA_FG4_LVL1 = "userDataKeyFg4L1";
    private final String USER_DATA_FG4_LVL2 = "userDataKeyFg4L2";
    private final String USER_DATA_FG4_LVL3 = "userDataKeyFg4L3";

    private int areaID = -1; // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4

    private int progress_Lvl_1 = 0;
    private int progress_Lvl_2 = 0;
    private int progress_Lvl_3 = 0;

    private Button btn_newArea;
    private Button btn_level1;
    private Button btn_level2;
    private Button btn_level3;

    private ImageView ivLock1;
    private ImageView ivLock2;
    private ImageView ivLock3;
    private ImageView ivRank;
    private ImageView ivIcon1;
    private ImageView ivIcon2;
    private ImageView ivIcon3;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui5_level_selection);
        this.setTitle("Levelstufe wählen");

        this.areaID = getIntent().getIntExtra(SELECTED_AREA, -1);
        btn_newArea = (Button) findViewById(R.id.button_newArea);
        btn_newArea.setText("Anderes Fachgebiet finden");
        btn_newArea.setOnClickListener(this);

        // This order is necessary. Don't change it.
        initElements();
        loadUserData();
        setStatus();
    }

    private void initElements() {

        // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
        btn_level1 = (Button) findViewById(R.id.button_level1);
        btn_level2 = (Button) findViewById(R.id.button_level2);
        btn_level3 = (Button) findViewById(R.id.button_level3);
        btn_level1.setText("Level 1");
        btn_level2.setText("Level 2");
        btn_level3.setText("Level 3");

        ivRank = (ImageView) findViewById(R.id.imageRank);
        ivLock1 = (ImageView) findViewById(R.id.lock1);
        ivLock2 = (ImageView) findViewById(R.id.lock2);
        ivLock3 = (ImageView) findViewById(R.id.lock3);
        ivIcon1 = (ImageView) findViewById(R.id.icon1);
        ivIcon2 = (ImageView) findViewById(R.id.icon2);
        ivIcon3 = (ImageView) findViewById(R.id.icon3);

        ivLock1.setImageResource(locko);
        ivLock2.setImageResource(lockc);
        ivLock3.setImageResource(lockc);
        ivRank.setImageResource(marker_area_blue);
        btn_level1.setBackgroundResource(button_bg_round);

        btn_level1.setOnClickListener(this);
        btn_level2.setOnClickListener(this);
        btn_level3.setOnClickListener(this);

        // Area
        TextView tv = (TextView) findViewById(R.id.textView_Area);
        if (this.areaID == 0){
            tv.setText("Gravitationsphysik");
            ivIcon1.setImageResource(image_area_physics);
            ivIcon2.setImageResource(image_area_physics_grey);
            ivIcon3.setImageResource(image_area_physics_grey);
        }
        else if (this.areaID == 1){
            tv.setText("Wirtschaftswissenschaft");
            ivIcon1.setImageResource(image_area_economic);
            ivIcon2.setImageResource(image_area_economic_grey);
            ivIcon3.setImageResource(image_area_economic_grey);
        }
        else if (this.areaID == 2){
            tv.setText("Software Engineering");
            ivIcon1.setImageResource(image_area_se);
            ivIcon2.setImageResource(image_area_se_grey);
            ivIcon3.setImageResource(image_area_se_grey);
        }
        else if (this.areaID == 3){
            tv.setText("Elektrotechnik");
            ivIcon1.setImageResource(image_area_electrical);
            ivIcon2.setImageResource(image_area_electrical_grey);
            ivIcon3.setImageResource(image_area_electrical_grey);
        }
        else if (this.areaID == 4){
            tv.setText("Soziologie");
            ivIcon1.setImageResource(image_area_sociology);
            ivIcon2.setImageResource(image_area_sociology_grey);
            ivIcon3.setImageResource(image_area_sociology_grey);
        }
    }

    // Used to get the highest reached level by calculating progress of predecessor
    private int getLevelStatus(){
        int lvl = 1;
        if(hasReachedHigherLvl(this.progress_Lvl_1)) lvl = 2;
        if(hasReachedHigherLvl(this.progress_Lvl_2)) lvl = 3;
        if(hasReachedHigherLvl(this.progress_Lvl_3)) lvl = 4;
        return lvl;
    }

    // Sets the symbols and buttons colors
    private void setStatus() {
        if(getLevelStatus() == 4){
            ivLock1.setImageResource(R.drawable.locko);
            ivLock2.setImageResource(R.drawable.locko);
            ivLock3.setImageResource(R.drawable.locko);

            ivRank.setImageResource(marker_area_blue3stars);

            btn_level2.setBackgroundResource(button_bg_round);
            btn_level3.setBackgroundResource(button_bg_round);

            if(this.areaID == 0){
                ivIcon1.setImageResource(image_area_physics);
                ivIcon2.setImageResource(image_area_physics);
                ivIcon3.setImageResource(image_area_physics);
            }
            else if(this.areaID == 1){
                ivIcon1.setImageResource(image_area_economic);
                ivIcon2.setImageResource(image_area_economic);
                ivIcon3.setImageResource(image_area_economic);
            }
            else if(this.areaID == 2){
                ivIcon1.setImageResource(image_area_se);
                ivIcon2.setImageResource(image_area_se);
                ivIcon3.setImageResource(image_area_se);
            }
            else if(this.areaID == 3){
                ivIcon1.setImageResource(image_area_electrical);
                ivIcon2.setImageResource(image_area_electrical);
                ivIcon3.setImageResource(image_area_electrical);
            }
            else if(this.areaID == 4){
                ivIcon1.setImageResource(image_area_sociology);
                ivIcon2.setImageResource(image_area_sociology);
                ivIcon3.setImageResource(image_area_sociology);
            }
        }
        else if(getLevelStatus() == 3){
            ivLock1.setImageResource(R.drawable.locko);
            ivLock2.setImageResource(R.drawable.locko);
            ivLock3.setImageResource(R.drawable.locko);

            ivRank.setImageResource(marker_area_blue2stars);

            btn_level2.setBackgroundResource(button_bg_round);
            btn_level3.setBackgroundResource(button_bg_round);

            if(this.areaID == 0){
                ivIcon1.setImageResource(image_area_physics);
                ivIcon2.setImageResource(image_area_physics);
                ivIcon3.setImageResource(image_area_physics);
            }
            else if(this.areaID == 1){
                ivIcon1.setImageResource(image_area_economic);
                ivIcon2.setImageResource(image_area_economic);
                ivIcon3.setImageResource(image_area_economic);
            }
            else if(this.areaID == 2){
                ivIcon1.setImageResource(image_area_se);
                ivIcon2.setImageResource(image_area_se);
                ivIcon3.setImageResource(image_area_se);
            }
            else if(this.areaID == 3){
                ivIcon1.setImageResource(image_area_electrical);
                ivIcon2.setImageResource(image_area_electrical);
                ivIcon3.setImageResource(image_area_electrical);
            }
            else if(this.areaID == 4){
                ivIcon1.setImageResource(image_area_sociology);
                ivIcon2.setImageResource(image_area_sociology);
                ivIcon3.setImageResource(image_area_sociology);
            }
        }
        else if(getLevelStatus() == 2){
            ivLock1.setImageResource(R.drawable.locko);
            ivLock2.setImageResource(R.drawable.locko);
            ivLock3.setImageResource(R.drawable.lockc);

            ivRank.setImageResource(marker_area_blue1star);

            btn_level2.setBackgroundResource(button_bg_round);
            btn_level3.setBackgroundResource(button_bg_round_unclickable);

            if(this.areaID == 0){
                ivIcon1.setImageResource(image_area_physics);
                ivIcon2.setImageResource(image_area_physics);
                ivIcon3.setImageResource(image_area_physics_grey);
            }
            else if(this.areaID == 1){
                ivIcon1.setImageResource(image_area_economic);
                ivIcon2.setImageResource(image_area_economic);
                ivIcon3.setImageResource(image_area_economic_grey);
            }
            else if(this.areaID == 2){
                ivIcon1.setImageResource(image_area_se);
                ivIcon2.setImageResource(image_area_se);
                ivIcon3.setImageResource(image_area_se_grey);
            }
            else if(this.areaID == 3){
                ivIcon1.setImageResource(image_area_electrical);
                ivIcon2.setImageResource(image_area_electrical);
                ivIcon3.setImageResource(image_area_electrical_grey);
            }
            else if(this.areaID == 4){
                ivIcon1.setImageResource(image_area_sociology);
                ivIcon2.setImageResource(image_area_sociology);
                ivIcon3.setImageResource(image_area_sociology_grey);
            }
        }
    }

    private boolean hasReachedHigherLvl(int progress){
        if(progress < LEVEL_UP) return false;
        else return true;
    }

    /*
     Load User Data selected by the area user has chosen
     */
    private void loadUserData(){
        try{
            SharedPreferences sharedPref = getSharedPreferences(USER_DATA_FILE_NAME, Context.MODE_PRIVATE);

            // Physik=0, Wirtschaft=1, SE=2, ETechnik=3, Soziologie=4
            if(this.areaID == 0){
                this.progress_Lvl_1 = sharedPref.getInt(USER_DATA_FG0_LVL1, 0);
                this.progress_Lvl_2 = sharedPref.getInt(USER_DATA_FG0_LVL2, 0);
                this.progress_Lvl_3 = sharedPref.getInt(USER_DATA_FG0_LVL3, 0);
            }
            else if(this.areaID == 1) {
                this.progress_Lvl_1 = sharedPref.getInt(USER_DATA_FG1_LVL1, 0);
                this.progress_Lvl_2 = sharedPref.getInt(USER_DATA_FG1_LVL2, 0);
                this.progress_Lvl_3 = sharedPref.getInt(USER_DATA_FG1_LVL3, 0);
            }
            else if(this.areaID == 2) {
                this.progress_Lvl_1 = sharedPref.getInt(USER_DATA_FG2_LVL1, 0);
                this.progress_Lvl_2 = sharedPref.getInt(USER_DATA_FG2_LVL2, 0);
                this.progress_Lvl_3 = sharedPref.getInt(USER_DATA_FG2_LVL3, 0);
            }
            else if(this.areaID == 3) {
                this.progress_Lvl_1 = sharedPref.getInt(USER_DATA_FG3_LVL1, 0);
                this.progress_Lvl_2 = sharedPref.getInt(USER_DATA_FG3_LVL2, 0);
                this.progress_Lvl_3 = sharedPref.getInt(USER_DATA_FG3_LVL3, 0);
            }
            else if(this.areaID == 4) {
                this.progress_Lvl_1 = sharedPref.getInt(USER_DATA_FG4_LVL1, 0);
                this.progress_Lvl_2 = sharedPref.getInt(USER_DATA_FG4_LVL2, 0);
                this.progress_Lvl_3 = sharedPref.getInt(USER_DATA_FG4_LVL3, 0);
            }
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "ERROR FOUND! In (GetUserData) SharedPreferences not loading.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_newArea) {
            Intent intent = new Intent(LevelSelection.this, GeoMap.class);
            startActivity(intent);
            this.finish();
        }
        else if(v.getId() == R.id.button_level1) {
            Intent intent = new Intent(LevelSelection.this, VocabularyView.class);
            intent.putExtra(SELECTED_AREA, this.areaID);
            intent.putExtra(SELECTED_LEVEL, 1);
            intent.putExtra(LEVEL_PROGRESS, this.progress_Lvl_1);
            startActivity(intent);
            this.finish();
        }
        else if(v.getId() == R.id.button_level2) {
            if(getLevelStatus() >= 2) {
                Intent intent = new Intent(LevelSelection.this, VocabularyView.class);
                intent.putExtra(SELECTED_AREA, this.areaID);
                intent.putExtra(SELECTED_LEVEL, 2);
                intent.putExtra(LEVEL_PROGRESS, this.progress_Lvl_2);
                startActivity(intent);
                this.finish();
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Dieser Level ist noch nicht freigeschaltet! " +
                                "Absolviere das Quiz auf Level 1 erfolgreich um diesen " +
                                "Level freizuschalten.", Toast.LENGTH_LONG).show();
            }
        }
        else if(v.getId() == R.id.button_level3) {
            if(getLevelStatus() >= 3) {
                Intent intent = new Intent(LevelSelection.this, VocabularyView.class);
                intent.putExtra(SELECTED_AREA, this.areaID);
                intent.putExtra(SELECTED_LEVEL, 3);
                intent.putExtra(LEVEL_PROGRESS, this.progress_Lvl_3);
                startActivity(intent);
                this.finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Dieser Level ist noch nicht freigeschaltet! " +
                                "Absolviere das Quiz auf Level 2 erfolgreich um diesen " +
                                "Level freizuschalten.", Toast.LENGTH_LONG).show();
            }
        }
    }
    public void onBackPressed(){
        Intent intent = new Intent(LevelSelection.this, GeoMap.class);
        startActivity(intent);
        this.finish();
    }
}