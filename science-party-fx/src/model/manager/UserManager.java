package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.database.DatabaseConnect;
import model.User;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Bastian on 29.02.2016.
 */
public class UserManager {

    public static ObservableMap<Integer, User> userMap = FXCollections.observableHashMap();


    public static String getAllUserJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/get/user/list";
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


    public static void refreshUserMap() throws IOException {
        ObservableMap<Integer, User> map = FXCollections.observableHashMap();
        String allUserJson = UserManager.getAllUserJson();

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(allUserJson, TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));

        for(User user: userList){
            map.put(user.getId(), user);
        }
        userMap=map;
    }



    public static void lockUser(int id, boolean lock) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/update/user";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Cookie", loginCookie);
        connection.setRequestProperty("Content-Type", "application/json");


        User updateUser = userMap.get(id);
        updateUser.setLocked(lock);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(updateUser);

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
        outWriter.write(jsonString);
        outWriter.flush();

        connection.getResponseMessage();
    }

    public static void deleteUser(int id) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/delete/user/" + id;
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);
        System.out.println(connection.getResponseMessage());

        refreshUserMap();
    }


    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de","araluen");


        UserManager.refreshUserMap();

        UserManager.deleteUser(15);





    }


}
