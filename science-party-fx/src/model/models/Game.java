package model.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.SimpleDateFormat;

/**
 * Created by Richard on 22.02.2016.
 */
public class Game {
    public Game(){

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    @JsonProperty("gameStatus")
    String gameStatus;

    @JsonProperty("whenCreated")
    long whenCreated;

    @JsonProperty("whenUpdated")
    long whenUpdated;

    @JsonProperty("topicId")
    int topicId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public long getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(long whenUpdated) {
        this.whenUpdated = whenUpdated;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Game(int id, String name, String gameStatus, long whenCreated, long whenUpdated, int topicId) {
        this.id = id;
        this.name = name;
        this.gameStatus = gameStatus;
        this.whenCreated = whenCreated;
        this.whenUpdated = whenUpdated;
        this.topicId = topicId;
    }

    public String getDay(){
        long seconds = Game.this.getWhenCreated();
        seconds = seconds*1000;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        StringBuilder sb = new StringBuilder(sdf.format(seconds));

        return sb.toString();
    }
}
