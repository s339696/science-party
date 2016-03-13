package model.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import model.Question;
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
public class QuestionManager {
    public static Map<Integer, Question> questionMap = new HashMap<>();

    public static String getAllQuestionsJson(int tid) throws IOException {
        String loginCookie = DatabaseConnect.getLoginCookie();

        String urlPath = "http://localhost:9000/ac/get/question/list/" + tid;
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

    public static void refreshQuestionMapPerTopic(int tid) throws IOException {
        String allQuestionsJson = QuestionManager.getAllQuestionsJson(tid);
        ObjectMapper mapper = new ObjectMapper();
        List<Question> questionList = mapper.readValue(allQuestionsJson, TypeFactory.defaultInstance().constructCollectionType(List.class, Question.class));

        for(Question question : questionList){
            questionMap.put(question.getId(), question);
        }

    }

    public static void main(String[] args) throws IOException {
        DatabaseConnect.setRecentUser("bastian95@live.de", "araluen");

        System.out.println(QuestionManager.getAllQuestionsJson(1));
        refreshQuestionMapPerTopic(1);

        //erste Frage zum ersten Topic
        System.out.println(questionMap.get(1).getText());
    }
}
