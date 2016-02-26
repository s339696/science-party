package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.User;

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
        showList();

    }

    User tim = new User(0, "Tim", "Tschauder", "linksradikal@gaude.net", "hose1234", new Date(), null);
    User user = new User(0,null,null,null,null,null,null);
    ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    private void showList(){
       ObservableList<String> data = FXCollections.observableArrayList("eins", "zwei", "drei");


        users.add(0,tim);

        for(User u: users){
            int i=0;
            data.add(i,u.getId() + ": " +u.getFirstName() + " " + u.getLastName());
            i++;
        }

       lv.setItems(data);

       System.out.println("Konsole klappt aber");


   }

    @FXML
    private void handleUserInSelect(){
        ObservableList<String> selectedItems = lv.getSelectionModel().getSelectedItems();
        String idString = selectedItems.get(0);
        String[] s = idString.split(":");
        int id = Integer.parseInt(s[0]);


        user = users.get(id);

        idLabel.textProperty().set(String.valueOf(user.getId()));
        firstNameLabel.textProperty().set(user.getFirstName());
        lastNameLabel.textProperty().set(user.getLastName());
        emailLabel.textProperty().set(user.getEmail());
        birthdateLabel.textProperty().set(user.getBirthDate());



        //System.out.println(lv.getSelectionModel().getSelectedItems().get(0));
        //System.out.println(id);

    }





}
