package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import static com.voctrainer.R.drawable.*;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MovingCounter extends AppCompatActivity implements SensorEventListener, View.OnClickListener{

    private final int MAXIMUM_OF_STEPS = 100;

    private Button btn_help;
    private Button btn_backToStart;
    private ImageView iV_go1, iV_go2, iV_go3;
    private TextView tv_steps;

    private SensorManager sensorManager;
    private Sensor mStepCounter, mStepDetector;

    private boolean isCounterSensorPresent, isDetectorSensorPresent;
    private int stepCount = 0, stepDetect = 0;
    private int iterationWalk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui2_moving_counter);
        this.setTitle("Schritte zählen");

        btn_help = (Button) findViewById(R.id.button_help);
        btn_help.setText("?");
        btn_help.setOnClickListener(this);

        this.stepDetect = getIntent().getIntExtra("countedSteps", 0);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        tv_steps = (TextView) findViewById(R.id.textView_steps);
        tv_steps.setText(String.valueOf(stepDetect));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        btn_backToStart = (Button) findViewById(R.id.button_backToStart);
        btn_backToStart.setText("zurück");
        btn_backToStart.setOnClickListener(this);

        iV_go1 = (ImageView) findViewById(R.id.imageView_go1);
        iV_go2 = (ImageView) findViewById(R.id.imageView_go2);
        iV_go3= (ImageView) findViewById(R.id.imageView_go3);

        iV_go1.setImageResource(person_go);
        iV_go2.setImageResource(person_go);
        iV_go3.setImageResource(person_go);

        iV_go1.setVisibility(View.INVISIBLE);
        iV_go2.setVisibility(View.VISIBLE);
        iV_go3.setVisibility(View.INVISIBLE);
        init();
    }

    private void init(){
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null){
            mStepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isDetectorSensorPresent = true;
        }
        else {
            tv_steps.setText("Detector Sensor is not present!");
            isDetectorSensorPresent = false;
        }
    }

    private void iterateWalk(){
        if(this.iterationWalk == 0) {
            iV_go1.setImageResource(person_go);
            iV_go2.setImageResource(person_go);
            iV_go3.setImageResource(person_go);
            personWalk(0);
            this.iterationWalk = 1;
        }
        else if(this.iterationWalk == 1){
            personWalk(1);
            this.iterationWalk = 2;
        }
        else if(this.iterationWalk == 2){
            personWalk(2);
            this.iterationWalk = 3;
        }
        else if(this.iterationWalk == 3){
            iV_go1.setImageResource(person_go2);
            iV_go2.setImageResource(person_go2);
            iV_go3.setImageResource(person_go2);
            personWalk(0);
            this.iterationWalk = 4;
        }
        else if(this.iterationWalk == 4){
            personWalk(1);
            this.iterationWalk = 5;
        }
        else if(this.iterationWalk == 5){
            personWalk(2);
            this.iterationWalk = 0;
        }
    }

    // 0,1 and 2
    private void personWalk(int personID){
        if(personID == 0) {
            iV_go1.setVisibility(View.VISIBLE);
            iV_go2.setVisibility(View.INVISIBLE);
            iV_go3.setVisibility(View.INVISIBLE);
        }
        else if(personID == 1) {
            iV_go1.setVisibility(View.INVISIBLE);
            iV_go2.setVisibility(View.VISIBLE);
            iV_go3.setVisibility(View.INVISIBLE);
        }
        else if(personID == 2) {
            iV_go1.setVisibility(View.INVISIBLE);
            iV_go2.setVisibility(View.INVISIBLE);
            iV_go3.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(MovingCounter.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(this.stepDetect >= MAXIMUM_OF_STEPS) goToGeoMap();
        if(sensorEvent.sensor == mStepDetector){
            stepDetect = (int) (stepDetect + sensorEvent.values[0]);
            tv_steps.setText(String.valueOf(stepDetect));
            iterateWalk();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
            sensorManager.registerListener(this, mStepDetector, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
            sensorManager.unregisterListener(this, mStepDetector);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_help) {
            Intent intent = new Intent(MovingCounter.this, Help.class);
            intent.putExtra("activity_id", 1); //MainActivity is ID = 0
            intent.putExtra("countedSteps", this.stepDetect);
            startActivity(intent);
        }
        else if(v.getId() == R.id.button_backToStart) {
            sensorManager.unregisterListener(this, mStepDetector);
            Intent intent = new Intent(MovingCounter.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private void goToGeoMap(){
        sensorManager.unregisterListener(this, mStepDetector);
        Intent intent = new Intent(MovingCounter.this, ViewSteps.class);
        startActivity(intent);
        this.finish();
    }
}