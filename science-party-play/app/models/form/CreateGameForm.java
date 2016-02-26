package models.form;

import play.data.validation.Constraints;

/**
 * Created by ewolf on 26.02.2016.
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
