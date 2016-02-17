package models.ebean;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User extends Model {

    @Id
    public Long id;

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    @Constraints.Required
    public String email;

    //public boolean done;

    public static Finder<Long,User> find = new Finder<>(User.class);
}
