package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Richard on 22.02.2016.
 */
public class UserController implements Initializable{

    @FXML
    private TextField tf;

    @FXML
    private ListView<String> lv;

   @FXML
   public void handleUpdate(){
       System.out.println(tf.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("klappt");
        showList();

    }


    @FXML
    private void showList(){
       ObservableList<String> data = FXCollections.observableArrayList("eins", "zwei", "drei");

       lv.setItems(data);

       System.out.println("Konsole klappt aber");

   }





}
