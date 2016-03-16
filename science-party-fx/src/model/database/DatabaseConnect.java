package model.database;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bastian on 29.02.2016.
 */
public class DatabaseConnect {
    public static ObjectNode recentUser = JsonNodeFactory.instance.objectNode();
    public static String serverAddress;

    public static void setRecentUser(String email, String password) {
        recentUser.put("email", email);
        recentUser.put("password", password);

    }

    public static void setServerAddress(String s){
        serverAddress =s;
    }

    public static boolean connectedToDatabase() throws IOException {
        String urlPath = serverAddress + "/login";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int respond = connection.getResponseCode();
        boolean connected = false;

        if(connection.getResponseCode()== HttpURLConnection.HTTP_OK){
            connected=true;
        }
        return connected;
    }



    public static String getLoginCookie() throws IOException {
        URL url = new URL(serverAddress + "/login");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());

        outWriter.write(recentUser.toString());
        outWriter.flush();

        System.out.println(connection.getResponseMessage());

        String cookie = connection.getHeaderField("Set-Cookie");

        return cookie;
    }

    public static void main(String[] args) throws IOException {

        DatabaseConnect.setRecentUser("bastian95@live.de","araluen");

        //DatabaseConnect.sendPost("user/list");

        System.out.println(DatabaseConnect.getLoginCookie());

    }
 }
