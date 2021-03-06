package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.models.Answer;
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
 * Contains the data requests to the play server und the mapping to answer objects.
 */
public class AnswerManager {
    /**
     * List for managing all answer objects.
     */
    public static ObservableList<Answer> answerList = FXCollections.observableArrayList();

    /**
     * Request for all answers.
     *
     * @param qid               the id of the question to which the answers belong
     * @return                  all answers as JSON string
     * @throws IOException      if an error occurs while communicating with the play server
     */
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

    /**
     * (Re-)fill the answerList with answer objects from the requested JSON string
     *
     * @param qid                   the id of the question to which the answers belong
     * @throws IOException          thrown if an error occurs while mapping the string to objects
     */
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

    /**
     * updates an answer objects in the database
     *
     * @param answer                the answer to update
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
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

    /**
     * deletes an answer object in the database
     *
     * @param answer            the answer to delete
     * @throws IOException      thrown if an error occurs while communicating with the play server
     */
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

    /**
     * inserts a new answer object to the database
     *
     * @param answer                answer object to add
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
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
