package models.ebean;


import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;

import javax.persistence.*;

@Entity
@Table(name="players")
public class Player extends Model {

    // Enum
    public enum PlayerStatus {
        @EnumValue("I") INVITED,
        @EnumValue("A") ACCEPTED,
        @EnumValue("D") DECLINE,
        @EnumValue("P") PLAYING,
        @EnumValue("L") LEFT,
        @EnumValue("F") FINISHED,
    }


    // Finder
    public static Model.Finder<Long,Player> find = new Model.Finder<>(Player.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="field_position")
    private int fieldPosition;

    @Column(name="player_status")
    private PlayerStatus playerStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    private Game game;

    public Long getId() {
        return id;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    public void setFieldPosition(int fieldPosition) {
        this.fieldPosition = fieldPosition;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
