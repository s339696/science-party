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


    public ObservableList<User> getAllUsers() throws IOException {
        ObservableList<User> allUsers = FXCollections.observableArrayList();

        String users = Database.sendPost("user/list");

        //den String users noch irgendwie zu ner Liste mappen

        System.out.println(users);

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(users, new TypeReference<List<User>>() {});

       // List<User> userList = mapper.convertValue(users, mapper.getTypeFactory().constructCollectionType(List.class, User.class));

        return allUsers;
    }

    public User getUserById(int id) throws IOException {
        String body = "user/" + id;

        String userString = Database.sendPost(body);

        System.out.println(userString);

       return userManager.makeUser(userString);
    }

    public User makeUser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        //User user = mapper.readValue(jsonString, User.class);
        User user = mapper.convertValue(jsonString, User.class);

        return user;
    }

    public static void main(String[] args) throws IOException {

        Database.setRecentUser("bastian95@live.de","56CF1CCC7D53E570FE333734BE911548");


        //String body = "[{\"id\":1,\"firstname\":\"Erik\",\"lastname\":\"Wolf\",\"birthday\":530924400000,\"email\":\"erik.wolf.29@gmail.com\",\"password\":\"81DC9BDB52D04DC20036DBD8313ED055\",\"author\":false,\"points\":0,\"locked\":false,\"whenCreated\":1457077383000,\"whenUpdated\":1457077432000,\"lastOnline\":1457077432000,\"players\":[],\"birthdayAsString\":\"29.10.1986\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]},{\"id\":2,\"firstname\":\"Sara\",\"lastname\":\"Klueber\",\"birthday\":812070000000,\"email\":\"sara.klueber@web.de\",\"password\":\"81DC9BDB52D04DC20036DBD8313ED055\",\"author\":false,\"points\":0,\"locked\":false,\"whenCreated\":1457077621000,\"whenUpdated\":1457077723000,\"lastOnline\":1457077723000,\"players\":[],\"birthdayAsString\":\"26.09.1995\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]},{\"id\":3,\"firstname\":\"Jan\",\"lastname\":\"Wolf\",\"birthday\":198111600000,\"email\":\"jan@wolf.de\",\"password\":\"81DC9BDB52D04DC20036DBD8313ED055\",\"author\":false,\"points\":0,\"locked\":false,\"whenCreated\":1457077750000,\"whenUpdated\":1457077752000,\"lastOnline\":1457077752000,\"players\":[],\"birthdayAsString\":\"12.04.1976\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]},{\"id\":4,\"firstname\":\"Richard\",\"lastname\":\"Klüpfel\",\"birthday\":842306400000,\"email\":\"richard@geht-dich-nix-an.de\",\"password\":\"1A7BAC8E6E8BC9B6E49CDF68C7ED50DD\",\"author\":false,\"points\":0,\"locked\":false,\"whenCreated\":1457081735000,\"whenUpdated\":1457081756000,\"lastOnline\":1457081755000,\"players\":[],\"birthdayAsString\":\"10.09.1996\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]},{\"id\":5,\"firstname\":\"Bastian\",\"lastname\":\"Wolz\",\"birthday\":788914800000,\"email\":\"bastian95@live.de\",\"password\":\"56CF1CCC7D53E570FE333734BE911548\",\"author\":true,\"points\":0,\"locked\":false,\"whenCreated\":1457085540000,\"whenUpdated\":1457085542000,\"lastOnline\":1457085542000,\"players\":[],\"birthdayAsString\":\"01.01.1994\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]},{\"id\":6,\"firstname\":\"Felix\",\"lastname\":\"Sauer\",\"birthday\":746143200000,\"email\":\"felix.sauer@stud-mail.uni-wuerzburg.de\",\"password\":\"598D4C200461B81522A3328565C25F7C\",\"author\":false,\"points\":0,\"locked\":false,\"whenCreated\":1457104633000,\"whenUpdated\":1457104698000,\"lastOnline\":1457104698000,\"players\":[],\"birthdayAsString\":\"24.08.1993\",\"friendRequests\":[],\"pendingGames\":[],\"friends\":[]}]\n";
        //         User u = userManager.makeUser(body);
       //System.out.println(u.getFirstname());

        //User u = userManager.getUserById(1);
        //System.out.println(u.getFirstname());

        userManager.getAllUsers();



    }






}
