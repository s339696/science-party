package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import model.database.DatabaseConnect;
import model.User;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Bastian on 29.02.2016.
 */
public class UserManager {

    public static Map<Integer, User> userMap = new HashMap<>();

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

    public static String getUserByIdJson(int id) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/get/user/" + id;
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

    public static void MakeUserMap() throws IOException {
        String allUserJson = UserManager.getAllUserJson();

        ObjectMapper mapper = new ObjectMapper();
        List<User> userList = mapper.readValue(allUserJson, TypeFactory.defaultInstance().constructCollectionType(List.class, User.class));

        for(User user: userList){
            userMap.put(user.getId(), user);
        }

    }

    public static void deleteUser(int id) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/delete/user/" + id;
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);

        

    }




    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de","araluen");


        UserManager.MakeUserMap();

        System.out.println(userMap.get(1).getFirstname());
        UserManager.deleteUser(15);



    }


}
