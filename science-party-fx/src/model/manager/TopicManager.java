package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.models.Topic;
import model.database.DatabaseConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Richard on 13.03.2016.
 *
 * Contains the data requests to the play server und the mapping to topic objects.
 */
public class TopicManager {
    /**
     * List for managing all topic objects.
     */
    public static ObservableList<Topic> topicList = FXCollections.observableArrayList();

    /**
     * Request for all topic.
     *
     * @return                          all topics as JSON string
     * @throws IOException              if an error occurs while communicating with the play server
     */
    public static String getAllTopicsJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/topic/list";
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

    /**
     * (Re-)fill the perkList with perk objects from the requested JSON string
     *
     * @throws IOException          thrown if an error occurs while mapping the string to objects
     */
    public static void refreshTopicList() throws IOException {
        ObservableList<Topic> list = FXCollections.observableArrayList();
        String allTopicsJson = TopicManager.getAllTopicsJson();
        ObjectMapper mapper = new ObjectMapper();
        List<Topic> topicList = mapper.readValue(allTopicsJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Topic.class));

        for(Topic topic : topicList){
            list.add(topic);
        }
        TopicManager.topicList =list;

    }

    /**
     * updates a topic objects in the database
     *
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
    public static void updateTopic(Topic topic) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(topic);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/update/topic";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Cookie", loginCookie);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
        outWriter.write(jsonString);
        outWriter.flush();

        connection.getResponseMessage();
    }

    /**
     * deletes a topic object in the database
     *
     * @throws IOException      thrown if an error occurs while communicating with the play server
     */
    public static void deleteTopic(Topic topic) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/delete/topic/" + topic.getId();
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);
        System.out.println(connection.getResponseMessage());
    }

    /**
     * inserts a new topic object to the database
     *
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
    public static void insertTopic(Topic topic) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(topic);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/insert/topic";
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);

        connection.setRequestMethod("PUT");

        connection.setRequestProperty("Cookie", loginCookie);
        connection.setRequestProperty("Content-Type", "application/json");

        OutputStreamWriter outWriter = new OutputStreamWriter(connection.getOutputStream());
        outWriter.write(jsonString);
        outWriter.flush();

        connection.getResponseMessage();
    }



    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");
        DatabaseConnect.setServerAddress("http://localhost:9000");

        Topic t =  new Topic();
        t.setId(1);

        deleteTopic(t);

    }

}
