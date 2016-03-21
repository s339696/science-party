package model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;

/**
 * Created by Richard on 22.02.2016.
 */
public class Question {

    public Question(){

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("text")
    String text;

    @JsonProperty("difficulty")
    int difficulty;

    @JsonProperty("topicId")
    int topicId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Question(int id, String text, int difficulty, int topicId) {
        this.id = id;
        this.text = text;
        this.difficulty = difficulty;
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return text;
    }
}
