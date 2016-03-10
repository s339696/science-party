package models.form;

import play.data.validation.Constraints;

import javax.validation.Constraint;

/**
 * Form to parse payload of a answer request
 */
public class AnswerForm {

    @Constraints.Required
    private int answerId;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }
}
