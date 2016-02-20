package models.ebean;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="topics")
public class Topic extends Model {

    // Finder
    public static Model.Finder<Long,Topic> find = new Model.Finder<>(Topic.class);

    // Columns
    @Id
    @GeneratedValue
    private Long id;

    String name;

    @OneToMany(mappedBy="topic", cascade= CascadeType.ALL)
    private List<Game> games;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
