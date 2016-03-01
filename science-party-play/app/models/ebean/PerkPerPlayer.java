package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Contains the perks a player can use per game.
 */
@Entity
@Table(name="perks_per_player")
public class PerkPerPlayer {

    // Finder
    public static Model.Finder<Long,PerkPerPlayer> find = new Model.Finder<>(PerkPerPlayer.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Player player;

    @ManyToOne
    private PerkPerUserAndTopic perk;

}
