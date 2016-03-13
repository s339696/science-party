package model;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * Created by Richard on 22.02.2016.
 */
public class Topic {
    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

    public Topic(){

    }

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

    public Topic(int id, String name) {

        this.id = id;
        this.name = name;
    }

}
