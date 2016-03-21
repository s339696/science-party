package model.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Richard on 22.02.2016.
 *
 * Blueprint for mapping JsonStrings to an Answer-Object.
 */
public class Answer {

    public Answer(){
    }

    @JsonProperty("id")
    int id;

    @JsonProperty("text")
    String text;

    @JsonProperty("correct")
    boolean correct;

    @JsonProperty("questionId")
    int questeionId;

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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public int getQuesteionId() {
        return questeionId;
    }

    public void setQuesteionId(int questeionId) {
        this.questeionId = questeionId;
    }

    public Answer(int id, String text, boolean correct, int questeionId) {
        this.id = id;
        this.text = text;
        this.correct = correct;
        this.questeionId = questeionId;
    }
}
