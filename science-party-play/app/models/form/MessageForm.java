package models.form;

import play.data.validation.Constraints;

/**
 * Form to parse a message
 */
public class MessageForm {

    private int[] toIds;

    @Constraints.Required
    String message;

    public int[] getToIds() {
        return toIds;
    }

    public void setToIds(int[] toIds) {
        this.toIds = toIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
