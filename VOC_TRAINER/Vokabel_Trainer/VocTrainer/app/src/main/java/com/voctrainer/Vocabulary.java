package com.voctrainer;
/*
    Mobile Interaction Design - Group 5
    VocTrainer 1.0
    von Fabrice S., Sara A., Garros S. und Sara M.
*/

import java.io.Serializable;
import java.util.Random;

/*
 This class describes a single Vocabulary with name, correct answer and two wrong answers.
 Must implements interface serializable to send it later to another activity via putExtra().
 */

public class Vocabulary implements Serializable {

    private String name = "";
    private String name_correct = "";
    private String name_wrong1 = "";
    private String name_wrong2 = "";
    private String givenAnswer = "";
    private int posName_correct = 0;
    private int posName_wrong1 = 0;
    private int posName_wrong2 = 0;

    public Vocabulary(String name, String correct, String wrong1, String wrong2){
        this.name = name;
        this.name_correct = correct;
        this.name_wrong1 = wrong1;
        this.name_wrong2 = wrong2;
    }

    // Getter
    public String getName(){
        return this.name;
    }
    public String getName_correct(){
        return this.name_correct;
    }
    public String getName_wrong1(){
        return this.name_wrong1;
    }
    public String getName_wrong2(){
        return this.name_wrong2;
    }
    public String getGivenAnswer(){
        return this.givenAnswer;
    }
    public int getCorrectAnswerPos(){
        return this.posName_correct;
    }
    public int getWrong1AnswerPos(){
        return this.posName_wrong1;
    }
    public int getWrong2AnswerPos(){
        return this.posName_wrong2;
    }

    // Setter
    public void setGivenAnswer(String answer){
        this.givenAnswer = answer;
    }
    public boolean checkAnswerWasCorrect(){
        return this.name_correct.equals(this.givenAnswer);
    }

    public void randomizeOrder(){
        int x = 0;
        int y = 0;
        int z = 0;
        Random rand = new Random();

        while(!((x != y)&&(x != z)&&(y != z))){
            x = rand.nextInt(3); // 0 , 1 or 2;
            y = rand.nextInt(3); // 0 , 1 or 2;
            z = rand.nextInt(3); // 0 , 1 or 2;
        }
        this.posName_correct = x;
        this.posName_wrong1 = y;
        this.posName_wrong2 = z;
    }
}
