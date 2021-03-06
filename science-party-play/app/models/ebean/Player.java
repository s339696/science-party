package models.ebean;


import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.EnumValue;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy="player", cascade= CascadeType.ALL)
    private List<PerkPerPlayer> perksPerPlayer;

    /**
     * Return the player object that matches the given game and user.
     *
     * @param game
     * @param user
     * @return
     */
    public static Player getPlayerOfGameAndUser(Game game, User user) {
        return Player.find.where()
                .eq("user_id", user.getId())
                .eq("game_id", game.getId())
                .findUnique();
    }

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

    public List<PerkPerPlayer> getPerksPerPlayer() {
        return perksPerPlayer;
    }

    public void setPerksPerPlayer(List<PerkPerPlayer> perksPerPlayer) {
        this.perksPerPlayer = perksPerPlayer;
    }

    @Override
    public String toString() {
        return getUser().getFirstname() + " " +  getUser().getLastname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return !(id != null ? !id.equals(player.id) : player.id != null);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
