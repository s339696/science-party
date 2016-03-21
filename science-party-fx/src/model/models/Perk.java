package model.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;

/**
 * Created by Richard on 22.02.2016.
 */
public class Perk {
    public Perk(){

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

    public Perk(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
