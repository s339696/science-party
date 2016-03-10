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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PerkPerTopic getPerkPerTopic() {
        return perkPerTopic;
    }

    public void setPerkPerTopic(PerkPerTopic perkPerTopic) {
        this.perkPerTopic = perkPerTopic;
    }
}
