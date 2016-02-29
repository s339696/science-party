package models.ebean;


import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="perks")
public class Perk extends Model {

    // Finder
    public static Finder<Long,Perk> find = new Finder<>(Perk.class);

    // Columns

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="perk_name")
    private String perkName;

    @Size(max=32)
    @Column(name="qr_code")
    private String qrCode;

    @JoinTable(name="user_has_perks")
    @ManyToMany(cascade=CascadeType.ALL)
    List<User> users;

    public String getPerkName() {
        return perkName;
    }

    public void setPerkName(String perkName) {
        this.perkName = perkName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
