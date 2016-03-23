package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.database.DatabaseConnect;
import model.models.User;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by Bastian on 29.02.2016.
 */
public class UserManager {

    public static ObservableList<User> userList = FXCollections.observableArrayList();


    public static String getAllUserJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/user/list";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        connection.setRequestProperty("Cookie", loginCookie);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        String jsonString = response.toString();

        return jsonString;
    }


    public static void refreshUserList() throws IOException {
        ObservableList<User> list = FXCollections.observableArrayList();
        String allUserJson = UserManager.getAllUserJson();

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(allUserJson, TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));

        for(User user: userList){
            list.add(user);
        }
        UserManager.userList =list;
    }



    public static void lockUser(int id, boolean lock) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/update/user";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Cookie", loginCookie);
        connection.setRequestProperty("Content-Type", "application/json");

        //hier muss der user mit der ID geholt werden!!!!
        User updateUser = new User();
        for (User u : userList) {
            if(u.getId()==id){
                updateUser=u;
            }
        }


        updateUser.setLocked(lock);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(updateUser);

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
        outWriter.write(jsonString);
        outWriter.flush();

        connection.getResponseMessage();
    }

    public static void makeAuthor(int id, boolean isAuthor) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/update/user";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Cookie", loginCookie);
        connection.setRequestProperty("Content-Type", "application/json");

        //hier muss der user mit der ID geholt werden!!!!
        User updateUser = new User();
        for (User u : userList) {
            if(u.getId()==id){
                updateUser=u;
            }
        }


        updateUser.setAuthor(isAuthor);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(updateUser);

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
        outWriter.write(jsonString);
        outWriter.flush();

        connection.getResponseMessage();
    }

    public static void deleteUser(int id) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/delete/user/" + id;
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);
        System.out.println(connection.getResponseMessage());

        refreshUserList();
    }


    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de","araluen");


        UserManager.refreshUserList();

        UserManager.deleteUser(15);





    }


}
