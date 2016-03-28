package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.models.Question;
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
 * Contains the data requests to the play server und the mapping to question objects.
 */
public class QuestionManager {
    /**
     * List for managing all question objects.
     */
    public static ObservableList<Question> questionList = FXCollections.observableArrayList();

    /**
     * Request for all question.
     *
     * @return                          all questions as JSON string
     * @throws IOException              if an error occurs while communicating with the play server
     */
    public static String getAllQuestionsJson(int tid) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/get/question/list/" + tid;
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
    public static void refreshQuestionListPerTopic(int tid) throws IOException {
        ObservableList<Question> list = FXCollections.observableArrayList();
        String allQuestionsJson = QuestionManager.getAllQuestionsJson(tid);
        ObjectMapper mapper = new ObjectMapper();
        List<Question> questionList = mapper.readValue(allQuestionsJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Question.class));

        for(Question question : questionList){
            list.add(question);
        }
        QuestionManager.questionList = list;
    }

    /**
     * updates a question objects in the database
     *
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
    public static void updateQuestion(Question question) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(question);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/update/question";
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
     * deletes a question object in the database
     *
     * @throws IOException      thrown if an error occurs while communicating with the play server
     */
    public static void deleteQuestion(Question question) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/delete/question/" + question.getId();
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        connection.setRequestProperty("Cookie", loginCookie);
        System.out.println(connection.getResponseMessage());

        refreshQuestionListPerTopic(question.getTopicId());
    }

    /**
     * inserts a new question object to the database
     *
     * @throws IOException          thrown if an error occurs while communicating with the play server
     */
    public static void insertQuestion(Question question) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(question);

        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = DatabaseConnect.serverAddress + "/ac/insert/question";
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

        Question question = new Question(1,"Wie ist die Antwort?", 20,1);
        QuestionManager.updateQuestion(question);



    }
}
