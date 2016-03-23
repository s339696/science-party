package model.database;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.javaws.Main;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Bastian on 29.02.2016.
 *
 * Basic database connection
 */
public class DatabaseConnect {
    public static ObjectNode recentUser = JsonNodeFactory.instance.objectNode();
    public static String serverAddress = "http://localhost:9000";

    /**
     * Save the login data in static values.
     *
     * @param email             email of the logged in user
     * @param password          password of the logged in user
     */
    public static void setRecentUser(String email, String password) {
        recentUser.put("email", email);
        recentUser.put("password", password);

    }

    /**
     * Save the server address in a static value.
     *
     * @param s                 server address
     */
    public static void setServerAddress(String s){
        serverAddress =s;
    }

    /**
     * Check if the database is reachable
     *
     * @return                  true if connected, false if not
     * @throws IOException      occurs if database server don't give an answer
     */
    public static boolean connectedToDatabase() throws IOException {
        String urlPath = (serverAddress + "/login");
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int respond = connection.getResponseCode();
        boolean connected = false;

        if(connection.getResponseCode()== HttpURLConnection.HTTP_OK){
            connected=true;
        }
        return connected;
    }


    /**
     * Asking the Playserver for logincookie.
     *
     * @return                  logincookie which is necessary to get data
     * @throws IOException      occurs if database is not reachable
     */
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
 }
