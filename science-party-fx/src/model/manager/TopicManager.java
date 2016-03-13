package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import model.Topic;
import model.database.DatabaseConnect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Richard on 13.03.2016.
 */
public class TopicManager {

    public static Map<Integer, Topic> topicMap = new HashMap<>();

    public static String getAllTopicsJson() throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/get/topic/list";
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

    public static void refreshTopicMap() throws IOException {
        String allTopicsJson = TopicManager.getAllTopicsJson();
        ObjectMapper mapper = new ObjectMapper();
        List<Topic> topicList = mapper.readValue(allTopicsJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Topic.class));

        for(Topic topic : topicList){
            topicMap.put(topic.getId(), topic);
        }

    }

    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");

        System.out.println(TopicManager.getAllTopicsJson());
        TopicManager.refreshTopicMap();
        System.out.println(topicMap.get(1).getName());
    }

}
