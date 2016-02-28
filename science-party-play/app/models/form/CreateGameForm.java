package models.form;

import play.data.validation.Constraints;

/**
 * Form to parse payload of a create game request
 */
public class CreateGameForm {

    @Constraints.Required()
    private int topicId;

    private int[] playerIds;

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int[] getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(int[] playerIds) {
        this.playerIds = playerIds;
    }
}
