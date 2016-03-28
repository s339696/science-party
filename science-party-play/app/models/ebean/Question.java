package models.ebean;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import controllers.Games;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question extends Model {

    // Finder
    public static Model.Finder<Long, Question> find = new Model.Finder<>(Question.class);

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 1000)
    private String text;

    private int difficulty;

    @ManyToOne
    private Topic topic;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    List<Answer> answers;

    @OneToMany(mappedBy = "activeQuestion", cascade = CascadeType.ALL)
    List<Game> activeInGame;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @JsonIgnore
    public Topic getTopic() {
        return topic;
    }

    @JsonIgnore
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @JsonIgnore
    public List<Answer> getAnswers() {
        return answers;
    }

    @JsonIgnore
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    @JsonIgnore
    public static Question getRandomQuestion(Topic topic) {
        return Question.find.where()
                .ieq("topic_id", topic.getId().toString())
                .orderBy("RAND()").setMaxRows(1).findUnique();
    }

    /**
     * Get Id of topic
     * @return
     */
    public Long getTopicId() {
        return topic.getId();
    }

    /**
     * Set topic by Id
     * @param id
     */
    public void setTopicId(Long id) {
        this.topic = topic.find.byId(id);
    }

    /**
     * Returns the corret answer of the question.
     *
     * @return
     */
    @JsonIgnore
    public Answer getCorrectAnswer() {
        return Answer.find.where()
                .ieq("question_id", this.getId().toString())
                .ieq("correct", "1").findUnique();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id != null ? id.equals(question.id) : question.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
