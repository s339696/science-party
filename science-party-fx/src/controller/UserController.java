package controller;

import javafx.beans.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.MapBinding;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
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
    private CheckBox checkBox;

    @FXML
    private Button deleteButton;


   @FXML
   public void handleUpdate(){
       System.out.println(search.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try {
            UserManager.refreshUserMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("klappt");

        UserManager.userMap.addListener(new MapChangeListener<Integer, User>() {
            @Override
            public void onChanged(Change<? extends Integer, ? extends User> change) {
                try {
                    showList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        try {
            showList();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    static ObservableList<String> presentationList = FXCollections.observableArrayList();
    @FXML
    private void showList() throws IOException {

        if(presentationList.size()>0){
            for(String s : presentationList){
                presentationList.remove(s);
            }
            presentationList.clear();
        }



        System.out.println(UserManager.userMap.get(1).getFirstname());


        for(User u : UserManager.userMap.values()){
            int i=0;
            presentationList.add(i,u.getId() + ": " +u.getFirstname() + " " + u.getLastname());
            i++;
        }

        Collections.sort(presentationList);

        lv.setItems(presentationList);


   }

    User presentedUser = new User();

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
        checkBox.setSelected(presentedUser.isLocked());

    }

    @FXML
    private void handleCheckBox() throws IOException {
        int id = presentedUser.getId();
        if(checkBox.isSelected()){
            UserManager.lockUser(id, true);
        }else{
            UserManager.lockUser(id, false);
        }
    }

    @FXML
    private void handleDeleteButton() throws IOException {
        int id = presentedUser.getId();
        UserManager.deleteUser(id);
        showList();

    }




}
