package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.JSONPObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableListValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Richard on 22.02.2016.
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

    @JsonProperty("birthday")
    Date birthday;

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

    @JsonProperty("whenCreated")
    Date whenCreated;

    @JsonProperty("whenUpdated")
    Date whenUpdated;

    @JsonProperty("lastOnline")
    Date lastOnline;

    @JsonProperty("players")
    List players;

    @JsonProperty("birthdayAsString")
    String birthdayAsString;

    @JsonProperty("friendRequests")
    List friendRequests;

    @JsonProperty("pendingGames")
    List pendingGames;

    @JsonProperty("friends")
    List friends;

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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public Date getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(Date whenCreated) {
        this.whenCreated = whenCreated;
    }

    public Date getWhenUpdated() {
        return whenUpdated;
    }

    public void setWhenUpdated(Date whenUpdated) {
        this.whenUpdated = whenUpdated;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public List getPlayers() {
        return players;
    }

    public void setPlayers(List players) {
        this.players = players;
    }

    public String getBirthdayAsString() {
        return birthdayAsString;
    }

    public void setBirthdayAsString(String birthdayAsString) {
        this.birthdayAsString = birthdayAsString;
    }

    public List getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List friendRequests) {
        this.friendRequests = friendRequests;
    }

    public List getPendingGames() {
        return pendingGames;
    }

    public void setPendingGames(List pendingGames) {
        this.pendingGames = pendingGames;
    }

    public List getFriends() {
        return friends;
    }

    public void setFriends(List friends) {
        this.friends = friends;
    }

    public User(int id, String firstname, String lastname, Date birthday, String email, String password, boolean author, int points, boolean locked, Date whenCreated, Date whenUpdated, Date lastOnline, List players, String birthdayAsString, List friendRequests, List pendingGames, List friends) {

        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.author = author;
        this.points = points;
        this.locked = locked;
        this.whenCreated = whenCreated;
        this.whenUpdated = whenUpdated;
        this.lastOnline = lastOnline;
        this.players = players;
        this.birthdayAsString = birthdayAsString;
        this.friendRequests = friendRequests;
        this.pendingGames = pendingGames;
        this.friends = friends;
    }

    public static void main(String[] args){

    }
}
