package models.ebean;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Column(unique = true)
    String name;

    @OneToMany(mappedBy="topic", cascade= CascadeType.ALL)
    private List<Game> games;

    @OneToMany(mappedBy="topic", cascade= CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy="topic", cascade= CascadeType.ALL)
    private List<PerkPerTopic> perksPerTopic;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public List<Game> getGames() {
        return games;
    }

    @JsonIgnore
    public void setGames(List<Game> games) {
        this.games = games;
    }

    @JsonIgnore
    public List<Question> getQuestions() {
        return questions;
    }

    @JsonIgnore
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return getName();
    }
}
