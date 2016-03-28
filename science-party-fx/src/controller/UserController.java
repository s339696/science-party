package controller;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import model.models.User;
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
    private CheckBox checkBox;

    @FXML
    private Button deleteButton;

    @FXML
    private CheckBox authorCheckBox;

    @FXML
    private Button cancelSearchButton;


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
        try {
            handleSearch();
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

    public void handleSearch() throws IOException {
        FilteredList<User> filteredData = new FilteredList<>(UserManager.userList, p -> true);

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if(user.toString().replaceAll("\\s+","").toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;

            });
        });

        SortedList<User> sortedData = new SortedList<User>(filteredData);

        lv.setItems(sortedData);
    }

   public void handleInput(){
       if (search.getText()==null||search.getText().isEmpty()){
           cancelSearchButton.setVisible(false);
       } else {
           cancelSearchButton.setVisible(true);
       }
   }

    public void cancelSearch(){
        cancelSearchButton.setVisible(false);
        search.setText("");
    }




}
