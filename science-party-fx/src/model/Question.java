package model;

import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public class Question {

    String text;
    int difficulty;

    public void setText(String text) {
        this.text = text;
    }

    public int getDifficulty() {

        return difficulty;
    }

    public Question(String text){
        this.text=text;
    }


    public String getText() {
        return this.text;
    }


    public Question newData(String s) {
        return new Question(s);
    }


    public void deleteData(Object o) {

    }


    public void editData() {

    }


    public void saveData(Label l, String text) {

    }

    public Question setDifficulty(int difficulty) {
        this.difficulty=difficulty;
        return Question.this;
    }

    public static void main(String[] args){
        Question q = new Question("test").setDifficulty(4);

        System.out.println(q.getDifficulty());
        System.out.println(q.getText());


    }
}
