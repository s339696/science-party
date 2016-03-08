package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="perks_per_user")
public class PerkPerUser extends Model {

    // Finder
    public static Finder<Long,PerkPerUser> find = new Finder<>(PerkPerUser.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private PerkPerTopic perkPerTopic;

    @OneToMany(mappedBy="perkPerUser", cascade= CascadeType.ALL)
    private List<PerkPerPlayer> playersPerPerk;
}
