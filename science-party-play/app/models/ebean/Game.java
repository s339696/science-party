package models.ebean;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.media.jfxmedia.events.PlayerStateEvent;

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

    private String name;

    @ManyToOne
    @Column(name="active_player")
    private Player activePlayer;

    @ManyToOne
    @Column(name="active_question")
    private Question activeQuestion;

    @Column(name="game_status")
    private GameStatus gameStatus;

    @CreatedTimestamp
    @Column(name="date_created", columnDefinition = "datetime")
    private Timestamp whenCreated;

    @UpdatedTimestamp
    @Column(name="date_updated", columnDefinition = "datetime")
    private Timestamp whenUpdated;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy="game", cascade=CascadeType.ALL)
    List<Player> players;

    /**
     * Returns the game with the given id.
     *
     * @param id
     * @return
     */
    public static Game getGameById(Long id) {
        return Game.find.byId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Topic getTopic() {
        return topic;
    }

    @JsonIgnore
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Long getTopicId() {
        return topic.getId();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    @JsonIgnore
    public Player getActivePlayer() {
        return activePlayer;
    }

    @JsonIgnore
    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    @JsonIgnore
    public Question getActiveQuestion() {
        return activeQuestion;
    }

    public void setActiveQuestion(Question activeQuestion) {
        this.activeQuestion = activeQuestion;
    }

    public Timestamp getWhenCreated() {
        return whenCreated;
    }

    public Timestamp getWhenUpdated() {
        return whenUpdated;
    }

    @JsonIgnore
    public boolean hasPlayingPlayer() {

        List<Player> player = Player.find
                .where()
                .eq("game_id", this.getId())
                .eq("player_status", "P").findList();

        return player.size() > 0 ? true : false;
    }

    /**
     * Change the id of the acitve player to the next player with status PLAYING
     * and assign a new random question.
     */
    @JsonIgnore
    public void nextTurn() {
        List<Player> players = Player.find.where()
                .eq("game_id", this.getId())
                .eq("player_status", Player.PlayerStatus.PLAYING)
                .orderBy().asc("id").findList();
        if (players.size() != 0) {
            int activeIndex = 0;
            int nextIndex = 0;
            for (Player player: players) {
                if (player.getId() == this.activePlayer.getId()) {
                    activeIndex = players.indexOf(player);
                }
            }

            if (activeIndex == players.size()-1) {
                nextIndex = 0;
            } else {
                nextIndex = activeIndex + 1;
            }

            this.setActiveQuestion(Question.getRandomQuestion(this.getTopic()));
            this.setActivePlayer(players.get(nextIndex));
            this.update();
        }
    }

    /*
     * METHODS TO MANAGE PLAYER OF A GAME
     */

    /**
     * Returns a list with all players.
     *
     * @return
     */
    @JsonIgnore
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Return the player for a given user to this game.
     *
     * @param user
     * @return
     */
    @JsonIgnore
    public Player getPlayerForUser(User user) {
        List<Player> players = this.getPlayers();
        for (Player player: players) {
            if (player.getUser().equals(user)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Returns a list with all playing players.
     *
     * @return
     */

    @JsonIgnore
    public List<Player> getPlayingPlayer() {
        return Player.find.where()
                .ieq("playerStatus", "P")
                .ieq("game_id", this.getId().toString()).findList();
    }
}
