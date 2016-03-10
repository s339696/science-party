package model.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    static UserManager userManager = new UserManager();


    public List<User> getAllUsers() throws IOException {
        String users = Database.sendPost("user/list");

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(users, new TypeReference<List<User>>() {});
        System.out.println(userList);

        return userList;
    }

    public User getUserById(int id) throws IOException {
        String body = "user/" + id;

        String userString = Database.sendPost(body);
        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(userString, User.class);

       return user;
    }



    public static void main(String[] args) throws IOException {

        Database.setRecentUser("bastian95@live.de","56CF1CCC7D53E570FE333734BE911548");

        User u = userManager.getUserById(1);
        System.out.println(u.getFirstname());

        List<User> userList = userManager.getAllUsers();
        System.out.println(userList.toString());
        System.out.println(userList.size());

    }






}
