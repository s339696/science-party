package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.Database;
import model.User;
import model.manager.UserManager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by Richard on 22.02.2016.
 */
public class UserController implements Initializable{

    @FXML
    private TextField search;

    @FXML
    private ListView<String> lv;

    @FXML
    private Label idLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label birthdateLabel;

    @FXML
    private ImageView imageUrl;


   @FXML
   public void handleUpdate(){
       System.out.println(search.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("klappt");
        try {
            showList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    User user = new User(0, null, null, null,null, null, false, 0, false, null, null, null, null, null, null, null, null);
    ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    private void showList() throws IOException {
       ObservableList<String> data = FXCollections.observableArrayList("eins", "zwei", "drei");

        //Schleife die alle User einträgt
        //users.add(0,tim);
        UserManager um = new UserManager();
        users = (ObservableList<User>) um.getAllUsers();


        Database.sendPost("user/list");
        for(User u: users){
            int i=0;
            data.add(i,u.getId() + ": " +u.getFirstname() + " " + u.getLastname());
            i++;
        }

       lv.setItems(data);

       System.out.println("Konsole klappt aber");


   }

   // private


    @FXML
    private void handleUserInSelect(){
        ObservableList<String> selectedItems = lv.getSelectionModel().getSelectedItems();
        String idString = selectedItems.get(0);
        String[] s = idString.split(":");
        int id = Integer.parseInt(s[0]);


        user = users.get(id);

        idLabel.textProperty().set(String.valueOf(user.getId()));
        firstNameLabel.textProperty().set(user.getFirstname());
        lastNameLabel.textProperty().set(user.getLastname());
        emailLabel.textProperty().set(user.getEmail());
        birthdateLabel.textProperty().set(user.getBirthdayAsString());

    }




}
