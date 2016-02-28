package models.ebean;

import com.avaje.ebean.Model;

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

    public Long getId() {
        return id;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public static Question getRandomQuestion(Topic topic) {
        return Question.find.where()
                .ieq("topic_id", topic.getId().toString())
                .orderBy("RAND()").setMaxRows(1).findUnique();
    }

    /**
     * Returns the corret answer of the question.
     *
     * @return
     */
    public Answer getCorrectAnswer() {
        return Answer.find.where()
                .ieq("question_id", this.getId().toString())
                .ieq("correct", "1").findUnique();
    }
}
