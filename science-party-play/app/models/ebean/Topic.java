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

    @OneToMany(mappedBy="topic", cascade= CascadeType.ALL)
    private List<Question> questions;

    public Long getId() {
        return id;
    }

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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
