package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Bastian on 29.02.2016.
 */
public class Database {
    public static ObjectNode recentUser = JsonNodeFactory.instance.objectNode();

    public static void setRecentUser(String email, String password) {
        recentUser.put("email", email);
        recentUser.put("password", password);

    }

    public static String sendPost(String body) throws IOException {

        // user/list    ->  gibt Liste aller User aus
        // user/:id     ->  gibt User mit bestimmter id aus


        String urlPath = "http://localhost:9000/ac/" + body;

        URL url = new URL(urlPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());

        outWriter.write(recentUser.toString());
        outWriter.flush();



        //System.out.println(login.toString());

        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        if(HttpResult == HttpURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }

            br.close();

            System.out.println(""+sb.toString());


        }else{
            System.out.println(connection.getResponseMessage());
        }

        return sb.toString();

    }

    public static void main(String[] args) throws IOException {

        Database.setRecentUser("bastian95@live.de","56CF1CCC7D53E570FE333734BE911548");

        Database.sendPost("user/1");
    }
 }
