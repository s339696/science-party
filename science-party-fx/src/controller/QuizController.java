package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.sun.xml.internal.bind.v2.model.annotation.AnnotationSource;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static controller.TopicButtonPressed.*;

/**
 * Created by Richard on 13.03.2016.
 */
public class QuizController implements Initializable {

    @FXML
    ListView<Topic> topicsListView;

    @FXML
    ListView<Question> questionsListView;

    @FXML
    TextArea questionBox;

    @FXML
    TextField difficultyField;

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

    @FXML
    Button addTopicButton;

    @FXML
    Button editTopicButton;

    @FXML
    Button deleteTopicButton;

    @FXML
    TextField TopicTextField;

    @FXML
    Button addQuestionButton;

    @FXML
    TextField addQuestionField;

    @FXML
    Button actionButton;

    @FXML
    Button deleteQuestionButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showTopics();
    }




    public void showTopics(){
        questionBox.setDisable(true);
        answerA.setDisable(true);
        answerB.setDisable(true);
        answerC.setDisable(true);
        answerD.setDisable(true);
        difficultyField.setDisable(true);
        radioA.setDisable(true);
        radioB.setDisable(true);
        radioC.setDisable(true);
        radioD.setDisable(true);



        try {
            TopicManager.refreshTopicList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        topicsListView.setItems(TopicManager.topicList);
    }


    @FXML
    public void handleTopicsInSelect() throws IOException {
      int id = topicsListView.getSelectionModel().getSelectedItem().getId();

        QuestionManager.refreshQuestionListPerTopic(id);

        questionsListView.setItems(QuestionManager.questionList);

    }

    List<Integer> idList = new ArrayList<>();


    @FXML
    public void handleQuestionsInSelect() throws IOException {
        questionBox.setDisable(false);
        difficultyField.setDisable(false);

        ToggleGroup group = new ToggleGroup();
        radioA.setToggleGroup(group);
        radioB.setToggleGroup(group);
        radioC.setToggleGroup(group);
        radioD.setToggleGroup(group);

        int qid = questionsListView.getSelectionModel().getSelectedItem().getId();

        for(Question question : QuestionManager.questionList){
            if(question.getId() == qid){
                questionBox.setText(question.getText());
                difficultyField.textProperty().set(String.valueOf(question.getDifficulty()));
            }
        }

        AnswerManager.refreshAnswerListPerQuestion(qid);

        answerA.textProperty().set("");
        answerB.textProperty().set("");
        answerC.textProperty().set("");
        answerD.textProperty().set("");

        answerA.setDisable(false);
        answerB.setDisable(false);
        answerC.setDisable(false);
        answerD.setDisable(false);

        radioA.setDisable(true);
        radioB.setDisable(true);
        radioC.setDisable(true);
        radioD.setDisable(true);

        int size = AnswerManager.answerList.size();
        System.out.println("Länge der AnswerList: " + size);

        for (Answer a :
                AnswerManager.answerList) {
            System.out.println(a.getText());
        }

       switch(size){
           case 4:
               radioD.setDisable(false);
               answerD.textProperty().set(AnswerManager.answerList.get(3).getText());
               radioD.setSelected(AnswerManager.answerList.get(3).isCorrect());
               idList.set(3, AnswerManager.answerList.get(3).getId());
           case 3:
               radioC.setDisable(false);
               answerC.textProperty().set(AnswerManager.answerList.get(2).getText());
               radioC.setSelected(AnswerManager.answerList.get(2).isCorrect());
               idList.set(2, AnswerManager.answerList.get(2).getId());
           case 2:
               radioB.setDisable(false);
               answerB.textProperty().set(AnswerManager.answerList.get(1).getText());
               radioB.setSelected(AnswerManager.answerList.get(1).isCorrect());
               idList.set(1, AnswerManager.answerList.get(1).getId());
           case 1:
               radioA.setDisable(false);
               answerA.textProperty().set(AnswerManager.answerList.get(0).getText());
               radioA.setSelected(AnswerManager.answerList.get(0).isCorrect());
               idList.set(0, AnswerManager.answerList.get(0).getId());
               break;
       }


    }
    TopicButtonPressed bttn = DEFAULT;

    public void openTopicEditorMode(TopicButtonPressed tbp, String text){
        TopicTextField.setVisible(true);
        actionButton.setVisible(true);
        actionButton.textProperty().set(text);
        bttn = tbp;
    }

    public void closeTopicEditorMode() throws IOException {
        TopicManager.refreshTopicList();
        topicsListView.setItems(TopicManager.topicList);
        TopicTextField.textProperty().set("");
        TopicTextField.setVisible(false);
        actionButton.setVisible(false);
        bttn=DEFAULT;
    }

    @FXML
    public void createNewTopic() throws IOException {
        openTopicEditorMode(ADD, "Hinzufügen");
    }

    @FXML
    public void editTopic(){
        Topic t = topicsListView.getSelectionModel().getSelectedItem();
        TopicTextField.textProperty().set(t.getName());
        openTopicEditorMode(EDIT, "Bearbeiten");
    }

    @FXML
    public void deleteTopic(){
        bttn = DELETE;
        openTopicEditorMode(DELETE, "Wirklich Löschen?");
        TopicTextField.setVisible(false);

    }

    @FXML
    public void doAction() throws IOException {
        Topic t = new Topic();
        switch (bttn){
            case ADD:
                t.setName(TopicTextField.textProperty().get());
                TopicManager.insertTopic(t);
                closeTopicEditorMode();
                break;
            case EDIT:
                t = topicsListView.getSelectionModel().getSelectedItem();
                t.setName(TopicTextField.textProperty().get());
                TopicManager.updateTopic(t);
                closeTopicEditorMode();
                break;
            case DELETE:
                t = topicsListView.getSelectionModel().getSelectedItem();
                TopicManager.deleteTopic(t);
                closeTopicEditorMode();
                break;
        }
    }

    @FXML
    public void createNewQuestion() throws IOException {

        if(!addQuestionField.isVisible()) {
            addQuestionField.setVisible(true);
            addQuestionButton.textProperty().set("Hinzufügen");
        } else {
            Question q = new Question();
            q.setText(addQuestionField.textProperty().get());
            q.setTopicId(topicsListView.getSelectionModel().getSelectedItem().getId());
            QuestionManager.insertQuestion(q);
            QuestionManager.refreshQuestionListPerTopic(q.getTopicId());
            questionsListView.setItems(QuestionManager.questionList);

            addQuestionField.textProperty().set("");
            addQuestionField.setVisible(false);
            addQuestionButton.textProperty().set("+Neue Frage");
        }
    }

    @FXML
    public void deleteQuestion() throws IOException {
        Question question = questionsListView.getSelectionModel().getSelectedItem();
        QuestionManager.deleteQuestion(question);
        questionsListView.setItems(QuestionManager.questionList);
    }


    @FXML
    public void activateRadioButtonA(){
        if(!answerA.textProperty().get().equals(null)){
            radioA.setDisable(false);
        } else{
            radioA.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonB(){
        if(!answerB.textProperty().get().equals(null)){
            radioB.setDisable(false);
        }else{
            radioB.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonC(){
        if(!answerC.textProperty().get().equals(null)){
            radioC.setDisable(false);
        }else{
            radioC.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonD(){
        if(!answerD.textProperty().get().equals(null)){
            radioD.setDisable(false);
        }else if(answerD.textProperty().get().equals(null) || answerD.textProperty().get().equals("")){
            radioD.setDisable(true);
        }
    }

    /*
    @FXML
    public void saveQuestion() throws IOException {
        Question question = questionsListView.getSelectionModel().getSelectedItem();
        question.setDifficulty(Integer.parseInt(difficultyField.textProperty().get()));
        question.setText(questionBox.textProperty().get());

        int qid = question.getId();

        Answer answer1 = new Answer();
        if(!answerA.textProperty().get().equals("")){
            answer1.setQuesteionId(qid);
            answer1.setId(idArray[0]);
            answer1.setText(answerA.textProperty().get());
            answer1.setCorrect(radioA.isSelected());
        }

        Answer answer2 = new Answer();
        if (idArray[1] != null) {
            answer2.setQuesteionId(qid);
            answer2.setId(idArray[1]);
            answer2.setText(answerB.textProperty().get());
            answer2.setCorrect(radioB.isSelected());
        }

        Answer answer3 = new Answer();
        if (idArray[2] != null) {
            answer3.setQuesteionId(qid);
            answer3.setId(idArray[2]);
            answer3.setText(answerC.textProperty().get());
            answer3.setCorrect(radioC.isSelected());
        }

        Answer answer4 = new Answer();
        if (idArray[3] != null) {
            answer4.setId(idArray[3]);
            answer4.setQuesteionId(qid);
            answer4.setText(answerD.textProperty().get());
            answer4.setCorrect(radioD.isSelected());
        }


        //hier noch Datenbank zeugs
        QuestionManager.updateQuestion(question);

        List<Answer> aList = new ArrayList<>();
        aList.addAll(Arrays.asList(answer1, answer2, answer3, answer4));

        //hier nicht answers sonder die Textfelder durch iterieren!!!!
        //dazu wieder Liste mit Textfeldern -> die dann durchgehen
        for(Answer answer : aList){
            System.out.println(answer.getText());
            if(answer.getText() != null){

                //hier noch irgendwie erstellen einbauen
                if (answer.getText().equals("")) {
                    //delete
                    AnswerManager.deleteAnswer(answer);
                }else if(false){
                    //create

                }else{
                    //update
                    AnswerManager.updateAnswer(answer);
                }
            }

        }
    }
    */

    @FXML
    public void saveQuestion() throws IOException {
        Question question = questionsListView.getSelectionModel().getSelectedItem();
        question.setDifficulty(Integer.parseInt(difficultyField.textProperty().get()));
        question.setText(questionBox.textProperty().get());
        int qid = question.getId();

        System.out.println("Inhalt AntwortFeld A: (" + answerA.textProperty().get() + ")");
        Answer answer = new Answer();
        if(AnswerManager.answerList.size()>=1){
            if(answerA.textProperty().get().equals("")){
                System.out.println("Antwort A wird gelöscht");
                //deleteAnswer
                answer = AnswerManager.answerList.get(0);
                AnswerManager.deleteAnswer(answer);

            } else {
                //updateAnswer
                System.out.println("Antwort A wird geupdatet");
                answer= AnswerManager.answerList.get(0);
                answer.setText(answerA.textProperty().get());
                answer.setCorrect(radioA.isSelected());
                AnswerManager.updateAnswer(answer);
                AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
            }
        } else if(!answerA.textProperty().get().equals("")){
            //createAnswer
            System.out.println("Antwort A wird erstellt");
            answer.setText(answerA.textProperty().get());
            answer.setQuesteionId(questionsListView.getSelectionModel().getSelectedItem().getId());
            AnswerManager.insertAnswer(answer);
            AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
            //handleQuestionsInSelect();
        }



    }

    public void createAnswer(String text){
        int i = AnswerManager.answerList.size();

        List<RadioButton> radioList = new ArrayList<>();
        radioList.addAll(Arrays.asList(radioA, radioB, radioC, radioD));

        Answer answer = new Answer();
        answer.setText(text);
        answer.setCorrect(radioList.get(i).isSelected());
        answer.setQuesteionId(questionsListView.getSelectionModel().getSelectedItem().getId());

        //datenbank zeugs
    }



}
