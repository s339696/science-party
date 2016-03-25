package model.models;

import com.fasterxml.jackson.annotation.JsonProperty;



/**
 * Created by Richard on 22.02.2016.
 *
 * Blueprint for mapping JsonStrings to a Topic-Object.
 */
public class Topic {

    public Topic(){

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("name")
    String name;

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

    @Override
    public String toString() {
        return name;
    }
}
