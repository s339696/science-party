package model.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Richard on 21.03.2016.
 */
public class Perk {
    public Perk(){

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("qrCode")
    String qrCode;

    @JsonProperty("name")
    String perkName;

    @JsonProperty("topicName")
    String topicName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPerkName() {
        return perkName;
    }

    public void setPerkName(String perkName) {
        this.perkName = perkName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
