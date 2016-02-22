package controller;



import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public interface Editor {

    public String getText();

    public Object newData(String s);

    public void deleteData(Object o);

    public void editData();

    public void saveData(Label l, String text);

}
