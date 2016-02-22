package controller;

import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public class Question implements Editor {

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

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Question newData(String s) {
        return new Question(s);
    }

    @Override
    public void deleteData(Object o) {

    }

    @Override
    public void editData() {

    }

    @Override
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
