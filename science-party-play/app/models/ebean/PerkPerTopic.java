package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="perks_per_topic")
public class PerkPerTopic extends Model {

    // Finder
    public static Finder<Long,PerkPerTopic> find = new Finder<>(PerkPerTopic.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    @Size(max=32)
    @Column(name="qr_code")
    private String qrCode;

    @ManyToOne
    private Perk perk;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy="perkPerTopic", cascade= CascadeType.ALL)
    private List<PerkPerUser> usersPerPerk;

    static public PerkPerTopic getPerkPerTopicByQrCode(String qrCode) {
        return PerkPerTopic.find.where()
                .ieq("qrCode", qrCode).findUnique();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    /**
     * Sets the QR Code of a perk related to a topic.
     * The QR Code is greated in following way:
     * --> md5hash(name_from_perk + " " + name_from_topic)
     *
     * @param qrCode
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Perk getPerk() {
        return perk;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

}
