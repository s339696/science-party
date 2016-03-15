package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.Answer;
import model.Question;
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
public class AnswerManager {
    public static Map<Integer, Answer> answerMap = new HashMap<>();

    public static String getAllAnswersJson(int qid) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/get/answer/list/" + qid;
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

    public static void refreshAnswerMapPerTopic(int qid) throws IOException {
        ObservableMap<Integer, Answer> map = FXCollections.observableHashMap();
        String allAnswersJson = AnswerManager.getAllAnswersJson(qid);
        ObjectMapper mapper = new ObjectMapper();
        List<Answer> answerList = mapper.readValue(allAnswersJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Answer.class));

        for(Answer answer : answerList){
            map.put(answer.getId(), answer);
        }
        answerMap=map;

    }

    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");

        System.out.println(AnswerManager.getAllAnswersJson(1));
        refreshAnswerMapPerTopic(1);


        System.out.println(answerMap.get(1).getText());
    }
}
