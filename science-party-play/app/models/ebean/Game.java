package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.Logger;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="games")
public class Game extends Model {

    // Enum
    public enum GameStatus {
        @EnumValue("P") PENDING,
        @EnumValue("A") ACTIVE,
        @EnumValue("F") FINISHED,
    }

    // Finder
    public static Model.Finder<Long,Game> find = new Model.Finder<>(Game.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="active_player")
    private int activePlayer;

    @Column(name="game_status")
    private GameStatus gameStatus;

    @CreatedTimestamp
    @Column(name="date_created")
    private Timestamp whenCreated;

    @UpdatedTimestamp
    @Column(name="date_updated")
    private Timestamp whenUpdated;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy="game", cascade= CascadeType.ALL)
    List<Player> players;

    public Long getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(int active_player) {
        this.activePlayer = active_player;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public Timestamp getWhenUpdated() {
        return whenUpdated;
    }

    public boolean hasPlayingPlayer() {

        List<Player> player = Player.find
                .where()
                .eq("game_id", this.getId())
                .eq("player_status", "P").findList();

        return player.size() > 0 ? true : false;
    }

    /**
     * Change the id of the acitve player to the next player with status PLAYING
     */
    public void setNextPlayerActive() {
        List<Player> players = Player.find.where()
                .eq("game_id", this.getId())
                .eq("player_status", Player.PlayerStatus.PLAYING)
                .orderBy().asc("id").findList();
        if (players.size() != 0) {
            int activeIndex = 0;
            int nextIndex = 0;
            for (Player player: players) {
                if (player.getId() == this.activePlayer) {
                    activeIndex = players.indexOf(player);
                }
            }

            if (activeIndex == players.size()-1) {
                nextIndex = 0;
            } else {
                nextIndex = activeIndex + 1;
            }

            this.setActivePlayer(players.get(nextIndex).getId().intValue());
            this.update();

            System.out.println("Active player: " + this.getActivePlayer());
        }
    }
}
