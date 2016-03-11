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



    ObservableList<String> presentationList = FXCollections.observableArrayList();
    @FXML
    private void showList() throws IOException {
        UserManager.MakeUserMap();
        System.out.println(UserManager.userMap.get(1).getFirstname());



        for(User u : UserManager.userMap.values()){
            int i=0;
            presentationList.add(i,u.getId() + ": " +u.getFirstname() + " " + u.getLastname());
            i++;
        }

        Collections.sort(presentationList);

       lv.setItems(presentationList);


   }

    User presentedUser = new User(0, null, null, null, null, false, 0,false,null);

    @FXML
    private void handleUserInSelect(){
        ObservableList<String> selectedItems = lv.getSelectionModel().getSelectedItems();
        String idString = selectedItems.get(0);
        String[] s = idString.split(":");
        int id = Integer.parseInt(s[0]);


        presentedUser = UserManager.userMap.get(id);

        idLabel.textProperty().set(String.valueOf(presentedUser.getId()));
        firstNameLabel.textProperty().set(presentedUser.getFirstname());
        lastNameLabel.textProperty().set(presentedUser.getLastname());
        emailLabel.textProperty().set(presentedUser.getEmail());
        birthdateLabel.textProperty().set(presentedUser.getBirthday());

    }




}
