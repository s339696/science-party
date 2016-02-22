package controller;

import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public class Answer implements Editor {


    String text;
    Boolean correct;


    public Answer(String text){
        this.text=text;
        this.correct=false;
    }

    public Answer setCorrect() {
        this.correct = true;
        return Answer.this;
    }

    public Boolean getCorrect() {

        return correct;
    }


    @Override
    public String getText() {
        return this.text;
    }



    @Override
    public Answer newData(String s) {
        return new Answer(s);
    }

    @Override
    public void deleteData(Object o) {
        // hier Daten aus der DB löschen
    }

    @Override
    public void editData() {

    }

    @Override
    public void saveData(Label l, String text) {
        l.textProperty().set(text);
    }


    public static void main(String[] args){

        Answer a = new Answer("test");
        System.out.println(a.getCorrect());

        Answer a2 = new Answer("test2").setCorrect();
        System.out.println(a2.getCorrect());
    }

}
