package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    (Final Version)
    von Fabrice S., Sara A., Garros S. und Sara M.

    ***************************
    *      Final Release      *
    *    Last Build: 23:42    *
    *       22.01.2022        *
    ***************************

    Conventions:
    PrimaryColorBlue(RGB):    0,  81, 159
    PrimaryColorGreen(RGB): 145, 167, 3
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_start;
    private Button btn_help;
    private int FINE_LOCATION_PERMISSION_CODE = 1; // ACCESS_FINE_LOCATION includes ACCESS_COARSE_LOCATION

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button) findViewById(R.id.button_start);
        btn_start.setText("Meine Schritte zählen");
        btn_start.setOnClickListener(this);

        btn_help = (Button) findViewById(R.id.button_help);
        btn_help.setText("?");
        btn_help.setOnClickListener(this);
        //Toast.makeText(getApplicationContext(),"MID Project: Voctrainer\n Version 1.0", Toast.LENGTH_LONG).show();
    }

    // Permission needed for GPS Location
    private void checkFineLocation(){

        // Already granted this permission!
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            goToMovingCounter();
        }
        else requestFineLocationPermission();
    }

    private void requestFineLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            new AlertDialog.Builder(this)
                    .setTitle("Erlaubnis erforderlich")
                    .setMessage("Die Erlaubnis auf deinen genauen Standort zuzugreifen ist für diese Applikation notwendig")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Beenden", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Erlaubnis auf den genauen Standort zuzugreifen erteilt", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erlaubnis auf den genauen Standort zuzugreifen verweigert", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void goToMovingCounter(){
        Intent intent = new Intent(MainActivity.this, MovingCounter.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        if(v.getId() == R.id.button_start) {
            checkFineLocation();
        } else if(v.getId() == R.id.button_help) {
            Intent intent = new Intent(MainActivity.this, Help.class);
            intent.putExtra("activity_id", 0); //MainActivity is ID = 0
            startActivity(intent);
        }
    }
}