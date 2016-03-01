package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Contains der perks a user has collected per topic.
 */
@Entity
@Table(name="perks_per_user_and_topic")
public class PerkPerUserAndTopic {

    // Finder
    public static Model.Finder<Long,PerkPerUserAndTopic> find = new Model.Finder<>(PerkPerUserAndTopic.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Perk perk;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy="perk", cascade= CascadeType.ALL)
    private List<PerkPerPlayer> playersPerPerk;
}
