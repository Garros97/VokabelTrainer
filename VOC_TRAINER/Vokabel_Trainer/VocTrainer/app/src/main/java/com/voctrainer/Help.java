package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class Help extends AppCompatActivity implements View.OnClickListener{

    private Button btn_back;
    private TextView tV_help;
    private int countedSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui1a_help);
        this.setTitle("Hilfe");

        btn_back = (Button) findViewById(R.id.button_back);
        btn_back.setText("zurück");
        btn_back.setOnClickListener(this);

        tV_help = (TextView) findViewById(R.id.tv_helpText);
        tV_help.setMovementMethod(new ScrollingMovementMethod());

        this.countedSteps = getIntent().getIntExtra("countedSteps", 0);

        if(getIntent().hasExtra("activity_id") == true) {
            int activity_id = getIntent().getExtras().getInt("activity_id");
            if(activity_id == 1) {
                tV_help.setText("Deine Schritt werden HIER gezählt und angezeigt.\n\nSobald du 100 Schritte\n" +
                        "gemacht hast, kannst du deinen Standort einsehen.");
            }
            else {
                tV_help.setText("Diese App wurde im Rahmen einer Verantstaltung (Mobile Interaction Design) der LUH entwickelt."
                + "\n\n" + "Ziel dieser Software ist es Angehörige der LUH zu motivieren sich mehr zu bewegen und "
                + "gleichzeitig den interdisziplinären Austausch von Fachausdrücken zu fördern."
                + "\n\nDie \"VocTrainer-App\" möchte körperliche Gesundheit und den interdisziplinären bzw. internationalen Wortschatz vergrößern."
                + " Die deutsch-englischen Vokabeln können nach einem Spaziergang in Ruhe an jedem Ort gelernt und abgefragt werden."
                + "\n\nAblauf\n\n" + "Mit dem Drücken des Buttons \"Meine Schritte zählen\" wird jeder Schritt von dir gezählt "
                + "bis du 100 erreicht hast. \n\nDu kannst dich anschließend in die Nähe eines der Fachbereiche der Uni begeben "
                + "und schaltest so einen Satz Vokabeln frei. Die Fachbereiche sind auf einer Karte mit blauen Kreisflächen markiert. "
                + "Probier es aus und bewege dich in einen der Kreise. "
                + "\n\n" + "Im Anschluss kannst du eine Levelstufe 1 bis 3 auswählen und anfangen englische Fachausdrücke zu lernen."
                + "\n\n" + "Wenn du meinst genug Vokabeln gelernt zu haben, dann prüfe "
                + "dein Wissen mit einem Quiz.\nHast du alles verstanden? Dann los! :)");
            }

        }
        tV_help.setOnClickListener(this);
    }

    private void goBack(){
        if(getIntent().hasExtra("activity_id") == true) {
            int activity_id = getIntent().getExtras().getInt("activity_id");
            if(activity_id == 1) {
                Intent intent = new Intent(Help.this, MovingCounter.class);
                intent.putExtra("countedSteps", this.countedSteps);
                startActivity(intent);
                this.finish();
            }
            else {
                Intent intent = new Intent(Help.this, MainActivity.class);
                startActivity(intent);
                this.finish();
            }

        }
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button_back) {
            goBack();
        }
    }

    public void onBackPressed(){
        goBack();
    }
}