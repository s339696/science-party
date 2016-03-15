package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Answer;
import model.Question;
import model.Topic;
import model.manager.AnswerManager;
import model.manager.QuestionManager;
import model.manager.TopicManager;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Richard on 13.03.2016.
 */
public class QuizController implements Initializable {

    @FXML
    ListView<String> topicsListView;

    @FXML
    ListView<String> questionsListView;

    @FXML
    TextArea questionBox;

    @FXML
    TextField answerA;

    @FXML
    TextField answerB;

    @FXML
    TextField answerC;

    @FXML
    TextField answerD;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            TopicManager.refreshTopicMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        showTopics();
    }

    public void showTopics(){
        ObservableList<String> list = FXCollections.observableArrayList();
        for(Topic topic : TopicManager.topicMap.values()){
            int i = 0;
            list.add(i, topic.getId() + ": " + topic.getName());
            i++;
        }
        Collections.sort(list);
        topicsListView.setItems(list);
    }

    @FXML
    public void handleTopicsInSelect() throws IOException {
       String selectedItem = topicsListView.getSelectionModel().getSelectedItem();
        String[] selectedId = selectedItem.split(":");
        int id = Integer.parseInt(selectedId[0]);

        QuestionManager.refreshQuestionMapPerTopic(id);

        ObservableList<String> list = FXCollections.observableArrayList();
        for(Question question : QuestionManager.questionMap.values()){
            int i = 0;
            list.add(i, question.getId() + ": " + question.getText());
            i++;
        }
        Collections.sort(list);
        questionsListView.setItems(list);
    }

    @FXML
    public void handleQuestionsInSelect() throws IOException {
        ObservableList<String> list = FXCollections.observableArrayList();

        String selectedItem = questionsListView.getSelectionModel().getSelectedItem();
        String[] selectedId = selectedItem.split(":");
        int id = Integer.parseInt(selectedId[0]);

        questionBox.setText(QuestionManager.questionMap.get(id).getText());

        AnswerManager.refreshAnswerMapPerTopic(id);



        for(Answer answer : AnswerManager.answerMap.values()){

            list.add(answer.getText());

        }

        Collections.sort(list);

        System.out.println(AnswerManager.answerMap.values().size());
        System.out.println(list.size());

        answerA.textProperty().set("");
        answerB.textProperty().set("");
        answerC.textProperty().set("");
        answerD.textProperty().set("");

        int size = list.size();

       switch(size){
           case 4:
               answerD.textProperty().set(list.get(3));
           case 3:
               answerC.textProperty().set(list.get(2));
           case 2:
               answerB.textProperty().set(list.get(1));
           case 1:
               answerA.textProperty().set(list.get(0));
               break;
       }


    }


}
