package model;

import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public class Topic implements Editor {

    String text;



    Topic topic = new Topic(text);


    public Topic(String text){
        this.text=text;
    }


    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public Topic newData(String s) {
        return new Topic(s);
    }

    @Override
    public void deleteData(Object o) {// hier Daten aus der DB löschen

    }

    @Override
    public void editData() {
        // Label bearbeitbar machen
    }

    @Override
    public void saveData(Label l, String text) {
        l.textProperty().set(text);
    }
}
