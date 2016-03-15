package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML
    RadioButton radioA;

    @FXML
    RadioButton radioB;

    @FXML
    RadioButton radioC;

    @FXML
    RadioButton radioD;


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
        ObservableList<Boolean> radioList = FXCollections.observableArrayList();

        ToggleGroup group = new ToggleGroup();
        radioA.setToggleGroup(group);
        radioB.setToggleGroup(group);
        radioC.setToggleGroup(group);
        radioD.setToggleGroup(group);



        String selectedItem = questionsListView.getSelectionModel().getSelectedItem();
        String[] selectedId = selectedItem.split(":");
        int id = Integer.parseInt(selectedId[0]);

        questionBox.setText(QuestionManager.questionMap.get(id).getText());

        AnswerManager.refreshAnswerMapPerTopic(id);



        for(Answer answer : AnswerManager.answerMap.values()){

            list.add(answer.getText());
            radioList.add(answer.isCorrect());
        }

        answerA.textProperty().set("");
        answerB.textProperty().set("");
        answerC.textProperty().set("");
        answerD.textProperty().set("");

        radioA.setDisable(true);
        radioB.setDisable(true);
        radioC.setDisable(true);
        radioD.setDisable(true);

        int size = list.size();

       switch(size){
           case 4:
               answerD.textProperty().set(list.get(3));
               radioD.setDisable(false);
               radioD.setSelected(radioList.get(3));
           case 3:
               answerC.textProperty().set(list.get(2));
               radioC.setDisable(false);
               radioC.setSelected(radioList.get(2));
           case 2:
               answerB.textProperty().set(list.get(1));
               radioB.setDisable(false);
               radioB.setSelected(radioList.get(1));
           case 1:
               answerA.textProperty().set(list.get(0));
               radioA.setDisable(false);
               radioA.setSelected(radioList.get(0));
               break;
       }


    }


}
