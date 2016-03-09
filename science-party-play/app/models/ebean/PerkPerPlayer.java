package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Contains the perks a player can use per game.
 */
@Entity
@Table(name="perks_per_player")
public class PerkPerPlayer extends Model {

    // Finder
    public static Model.Finder<Long,PerkPerPlayer> find = new Model.Finder<>(PerkPerPlayer.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private PerkPerUser perkPerUser;

    boolean used;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PerkPerUser getPerkPerUser() {
        return perkPerUser;
    }

    public void setPerkPerUser(PerkPerUser perkPerUser) {
        this.perkPerUser = perkPerUser;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
