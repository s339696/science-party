package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import model.Answer;
import model.database.DatabaseConnect;

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
 * Created by Richard on 13.03.2016.
 */
public class AnswerManager {
    public static ObservableList<Answer> answerList = FXCollections.observableArrayList();

    public static String getAllAnswersJson(int qid) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/answer/list/" + qid;
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

    public static void refreshAnswerListPerQuestion(int qid) throws IOException {
        ObservableList<Answer> list = FXCollections.observableArrayList();
        String allAnswersJson = AnswerManager.getAllAnswersJson(qid);
        ObjectMapper mapper = new ObjectMapper();
        List<Answer> answerList = mapper.readValue(allAnswersJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Answer.class));

        for(Answer answer : answerList){
            list.add(answer);
        }
        AnswerManager.answerList = list;

    }


    public static void updateAnswer(Answer answer) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(answer);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/update/answer";
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

    public static void deleteAnswer(Answer answer) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/delete/answer/" + answer.getId();
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);
        System.out.println(connection.getResponseMessage());

        refreshAnswerListPerQuestion(answer.getQuesteionId());
    }

    public static void insertAnswer(Answer answer) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(answer);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/insert/answer";
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

        refreshAnswerListPerQuestion(1);
        System.out.println(answerList.toString());


    }
}
