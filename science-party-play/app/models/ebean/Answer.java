package models.ebean;


import com.avaje.ebean.Model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="answers")
public class Answer extends Model {

    // Finder
    public static Model.Finder<Long,Answer> find = new Model.Finder<>(Answer.class);

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 255)
    private String text;

    private boolean correct;

    @ManyToOne
    private Question question;

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
