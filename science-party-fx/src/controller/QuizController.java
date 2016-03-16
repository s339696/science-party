package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
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
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ResourceBundle;

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
    Button addQuestionButton;


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

    Integer[] idArray = new Integer[4];

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

        answerA.setDisable(true);
        answerB.setDisable(true);
        answerC.setDisable(true);
        answerD.setDisable(true);

        radioA.setDisable(true);
        radioB.setDisable(true);
        radioC.setDisable(true);
        radioD.setDisable(true);

        int size = AnswerManager.answerList.size();

       switch(size){
           case 4:
               answerD.setDisable(false);
               radioD.setDisable(false);
               answerD.textProperty().set(AnswerManager.answerList.get(3).getText());
               radioD.setSelected(AnswerManager.answerList.get(3).isCorrect());
               idArray[3] = AnswerManager.answerList.get(3).getId();
           case 3:
               answerC.setDisable(false);
               radioC.setDisable(false);
               answerC.textProperty().set(AnswerManager.answerList.get(2).getText());
               radioC.setSelected(AnswerManager.answerList.get(2).isCorrect());
               idArray[2] = AnswerManager.answerList.get(2).getId();
           case 2:
               answerB.setDisable(false);
               radioB.setDisable(false);
               answerB.textProperty().set(AnswerManager.answerList.get(1).getText());
               radioB.setSelected(AnswerManager.answerList.get(1).isCorrect());
               idArray[1] = AnswerManager.answerList.get(1).getId();
           case 1:
               answerA.setDisable(false);
               radioA.setDisable(false);
               answerA.textProperty().set(AnswerManager.answerList.get(0).getText());
               radioA.setSelected(AnswerManager.answerList.get(0).isCorrect());
               idArray[0] = AnswerManager.answerList.get(0).getId();
               break;
       }


    }

    @FXML
    public void createNewTopic() throws IOException {
        /*topicsListView.setCellFactory(TextFieldListCell.forListView());
        topicsListView.setEditable(true);
        topicsListView.getItems().add(0,"hier Text eingeben");
        topicsIdList.add(0,null);
        String text = topicsListView.getItems().get(0);

        System.out.println(text);


        Topic topic = new Topic();
        topic.setName("neues Thema");
        System.out.println(topic.getName());


        showTopics();
*/
    }

    @FXML
    public void saveQuestion() throws IOException {
       /*
        Question question = new Question();
        question.setId(questionsIdList.get(questionsListView.getSelectionModel().getSelectedIndex()));
        question.setTopicId(topicsIdList.get(topicsListView.getSelectionModel().getSelectedIndex()));
        question.setDifficulty(Integer.parseInt(difficultyField.textProperty().get()));
        question.setText(questionBox.textProperty().get());

        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(question);
        System.out.println(s);

        int index = questionsListView.getSelectionModel().getSelectedIndex();
        int id = questionsIdList.get(index);


        Answer answer1 = new Answer();
        answer1.setQuesteionId(id);
        answer1.setText(answerA.textProperty().get());
        answer1.setCorrect(radioA.isSelected());

        Answer answer2 = new Answer();
        answer2.setQuesteionId(id);
        answer2.setText(answerB.textProperty().get());
        answer2.setCorrect(radioB.isSelected());

        Answer answer3 = new Answer();
        answer3.setQuesteionId(id);
        answer3.setText(answerC.textProperty().get());
        answer3.setCorrect(radioC.isSelected());

        Answer answer4 = new Answer();
        answer4.setQuesteionId(id);
        answer4.setText(answerD.textProperty().get());
        answer4.setCorrect(radioD.isSelected());

        //hier noch Datenbank zeugs

        AnswerManager.updateAnswer(answer1);
        AnswerManager.updateAnswer(answer2);
        AnswerManager.updateAnswer(answer3);
        AnswerManager.updateAnswer(answer4);

        QuestionManager.updateQuestion(question);

*/

    }



}
