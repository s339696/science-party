package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
    private ListView<User> lv;

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
    private CheckBox checkBox;

    @FXML
    private Button deleteButton;

    @FXML
    private CheckBox authorCheckBox;


   @FXML
   public void handleUpdate(){
       System.out.println(search.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            UserManager.refreshUserList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("klappt");




        try {
            showList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





    @FXML
    private void showList() throws IOException {

        lv.setItems(UserManager.userList);


   }

    User presentedUser = new User();

    @FXML
    private void handleUserInSelect(){
        presentedUser = lv.getSelectionModel().getSelectedItem();

        idLabel.textProperty().set(String.valueOf(presentedUser.getId()));
        firstNameLabel.textProperty().set(presentedUser.getFirstname());
        lastNameLabel.textProperty().set(presentedUser.getLastname());
        emailLabel.textProperty().set(presentedUser.getEmail());
        birthdateLabel.textProperty().set(presentedUser.getBirthday());
        checkBox.setSelected(presentedUser.isLocked());
        authorCheckBox.setSelected(presentedUser.isAuthor());

    }

    @FXML
    private void handleCheckBox() throws IOException {
        int id = presentedUser.getId();

        if(checkBox.isSelected()){
            System.out.println(id);
            UserManager.lockUser(id, true);
        }else{
            System.out.println(id);
            UserManager.lockUser(id, false);
        }
    }

    @FXML
    private void handleDeleteButton() throws IOException {
        int id = presentedUser.getId();
        UserManager.deleteUser(id);
        showList();

    }

    @FXML
    private void handleAuthorCheckBox() throws IOException {
        int id = presentedUser.getId();

        if(authorCheckBox.isSelected()){
            System.out.println(id);
            UserManager.makeAuthor(id, true);
        }else{
            System.out.println(id);
            UserManager.makeAuthor(id, false);
            System.out.println("Nutzer ist kein Autor");
        }
    }




}
