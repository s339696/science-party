package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Database;
import model.User;

import java.io.IOException;
import java.util.List;


/**
 * Created by Bastian on 29.02.2016.
 */
public class UserManager {

    Database database = new Database();

    public ObservableList<User> getAllUsers() throws IOException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        ObjectNode recentUser = JsonNodeFactory.instance.objectNode();
        recentUser.put("email", "bastian95@live.de");
        recentUser.put("password", "56CF1CCC7D53E570FE333734BE911548");


        database.sendPost(recentUser, "user/list");



        return allUsers;
    }


    public User makeUser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonString, User.class);


        return user;
    }






}
