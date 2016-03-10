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

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy="perk", cascade= CascadeType.ALL)
    private List<PerkPerTopic> perksPerTopic;

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
}
