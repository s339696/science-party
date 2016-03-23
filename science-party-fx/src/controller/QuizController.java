package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.models.Answer;
import model.models.Question;
import model.models.Topic;
import model.manager.AnswerManager;
import model.manager.QuestionManager;
import model.manager.TopicManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static controller.TopicButtonPressed.*;

/**
 * Created by Richard on 13.03.2016.
 */
public class QuizController implements Initializable {

    @FXML
    private ListView<Topic> topicsListView;

    @FXML
    private ListView<Question> questionsListView;

    @FXML
    private TextArea questionBox;

    @FXML
    private TextField difficultyField;

    @FXML
    private TextField answerA;

    @FXML
    private TextField answerB;

    @FXML
    private TextField answerC;

    @FXML
    private TextField answerD;

    @FXML
    private RadioButton radioA;

    @FXML
    private RadioButton radioB;

    @FXML
    private RadioButton radioC;

    @FXML
    private RadioButton radioD;

    @FXML
    Button addTopicButton;

    @FXML
    Button editTopicButton;

    @FXML
    Button deleteTopicButton;

    @FXML
    private TextField TopicTextField;

    @FXML
    private Button addQuestionButton;

    @FXML
    private TextField addQuestionField;

    @FXML
    private Button actionButton;

    @FXML
    Button deleteQuestionButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showTopics();

        ToggleGroup group = new ToggleGroup();
        radioA.setToggleGroup(group);
        radioB.setToggleGroup(group);
        radioC.setToggleGroup(group);
        radioD.setToggleGroup(group);

        questionBox.setWrapText(true);
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


    @FXML
    public void handleQuestionsInSelect() throws IOException {
        questionBox.setDisable(false);
        difficultyField.setDisable(false);

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

       switch(size){
           case 4:
               radioD.setDisable(false);
               answerD.textProperty().set(AnswerManager.answerList.get(3).getText());
               radioD.setSelected(AnswerManager.answerList.get(3).isCorrect());
               //idList.add(3, AnswerManager.answerList.get(3).getId());
           case 3:
               radioC.setDisable(false);
               answerC.textProperty().set(AnswerManager.answerList.get(2).getText());
               radioC.setSelected(AnswerManager.answerList.get(2).isCorrect());
               //idList.add(2, AnswerManager.answerList.get(2).getId());
           case 2:
               radioB.setDisable(false);
               answerB.textProperty().set(AnswerManager.answerList.get(1).getText());
               radioB.setSelected(AnswerManager.answerList.get(1).isCorrect());
               //idList.add(1, AnswerManager.answerList.get(1).getId());
           case 1:
               radioA.setDisable(false);
               answerA.textProperty().set(AnswerManager.answerList.get(0).getText());
               radioA.setSelected(AnswerManager.answerList.get(0).isCorrect());
               //idList.add(0, AnswerManager.answerList.get(0).getId());
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
        if(answerA.getText().length()!=0){
            radioA.setDisable(false);
        } else{
            radioA.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonB(){
        if(answerB.getText().length()!=0){
            radioB.setDisable(false);
        }else{
            radioB.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonC(){
        if(answerC.getText().length()!=0){
            radioC.setDisable(false);
        }else{
            radioC.setDisable(true);
        }
    }
    @FXML
    public void activateRadioButtonD(){
        if(answerD.getText().length()!=0){
            radioD.setDisable(false);
        }else if(answerD.textProperty().get().equals(null) || answerD.textProperty().get().equals("")){
            radioD.setDisable(true);
        }
    }

    @FXML
    public void saveQuestion() throws IOException {
        Question question = questionsListView.getSelectionModel().getSelectedItem();
        question.setDifficulty(Integer.parseInt(difficultyField.textProperty().get()));
        question.setText(questionBox.textProperty().get());
        QuestionManager.updateQuestion(question);

        handleAnswerA();
        handleAnswerB();
        handleAnswerC();
        handleAnswerD();
    }

    public void handleAnswerA() throws IOException {
        System.out.println("Inhalt AntwortFeld A: (" + answerA.textProperty().get() + ")");
        Answer answer = new Answer();
        if(AnswerManager.answerList.size()>=1){
            if(answerA.textProperty().get().equals("")){
                System.out.println("Antwort A wird gelöscht");
                //deleteAnswer
                answer = AnswerManager.answerList.get(0);
                AnswerManager.deleteAnswer(answer);
                handleQuestionsInSelect();
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

        }
    }

    public void handleAnswerB() throws IOException {
        System.out.println("Inhalt AntwortFeld B: (" + answerB.textProperty().get() + ")");
        Answer answer = new Answer();
        if(AnswerManager.answerList.size()>=2){
            if(answerB.textProperty().get().equals("")){
                System.out.println("Antwort B wird gelöscht");
                //deleteAnswer
                answer = AnswerManager.answerList.get(1);
                AnswerManager.deleteAnswer(answer);
                handleQuestionsInSelect();
            } else {
                //updateAnswer
                System.out.println("Antwort B wird geupdatet");
                answer= AnswerManager.answerList.get(1);
                answer.setText(answerB.textProperty().get());
                answer.setCorrect(radioB.isSelected());
                AnswerManager.updateAnswer(answer);
                AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
            }
        } else if(!answerB.textProperty().get().equals("")){
            //createAnswer
            System.out.println("Antwort B wird erstellt");
            answer.setText(answerB.textProperty().get());
            answer.setQuesteionId(questionsListView.getSelectionModel().getSelectedItem().getId());
            answer.setCorrect(radioB.isSelected());
            AnswerManager.insertAnswer(answer);
            AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
        }
    }

    public void handleAnswerC() throws IOException {
        System.out.println("Inhalt AntwortFeld C: (" + answerC.textProperty().get() + ")");
        Answer answer = new Answer();
        if(AnswerManager.answerList.size()>=3){
            if(answerC.textProperty().get().equals("")){
                System.out.println("Antwort C wird gelöscht");
                //deleteAnswer
                answer = AnswerManager.answerList.get(2);
                AnswerManager.deleteAnswer(answer);
                handleQuestionsInSelect();
            } else {
                //updateAnswer
                System.out.println("Antwort C wird geupdatet");
                answer= AnswerManager.answerList.get(2);
                answer.setText(answerC.textProperty().get());
                answer.setCorrect(radioC.isSelected());
                AnswerManager.updateAnswer(answer);
                AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
            }
        } else if(!answerC.textProperty().get().equals("")){
            //createAnswer
            System.out.println("Antwort C wird erstellt");
            answer.setText(answerC.textProperty().get());
            answer.setQuesteionId(questionsListView.getSelectionModel().getSelectedItem().getId());
            AnswerManager.insertAnswer(answer);
            AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
        }
    }

    public void handleAnswerD() throws IOException {
        System.out.println("Inhalt AntwortFeld D: (" + answerD.textProperty().get() + ")");
        Answer answer = new Answer();
        if(AnswerManager.answerList.size()>=4){
            if(answerD.textProperty().get().equals("")){
                System.out.println("Antwort D wird gelöscht");
                //deleteAnswer
                answer = AnswerManager.answerList.get(3);
                AnswerManager.deleteAnswer(answer);
                handleQuestionsInSelect();
            } else {
                //updateAnswer
                System.out.println("Antwort D wird geupdatet");
                answer= AnswerManager.answerList.get(3);
                answer.setText(answerD.textProperty().get());
                answer.setCorrect(radioD.isSelected());
                AnswerManager.updateAnswer(answer);
                AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
            }
        } else if(!answerD.textProperty().get().equals("")){
            //createAnswer
            System.out.println("Antwort D wird erstellt");
            answer.setText(answerD.textProperty().get());
            answer.setQuesteionId(questionsListView.getSelectionModel().getSelectedItem().getId());
            AnswerManager.insertAnswer(answer);
            AnswerManager.refreshAnswerListPerQuestion(questionsListView.getSelectionModel().getSelectedItem().getId());
        }
    }
}
