package model.models;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by Richard on 22.02.2016.
 *
 * Blueprint for mapping JsonStrings to an User-Object.
 */
public class User {
    public User(){

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("firstname")
    String firstname;

    @JsonProperty("lastname")
    String lastname;

    @JsonProperty("email")
    String email;

    @JsonProperty("password")
    String password;

    @JsonProperty("author")
    boolean author;

    @JsonProperty("points")
    int points;

    @JsonProperty("locked")
    boolean locked;

    @JsonProperty("birthday")
    String birthday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthor() {
        return author;
    }

    public void setAuthor(boolean author) {
        this.author = author;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User(int id, String firstname, String lastname, String email, String password, boolean author, int points, boolean locked, String birthday) {

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.author = author;
        this.points = points;
        this.locked = locked;
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
}
