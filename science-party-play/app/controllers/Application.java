package controllers;

import com.avaje.ebean.Ebean;
import models.ebean.User;
import play.*;
import play.mvc.*;
import play.Logger;

import views.html.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Application extends Controller {

    public Result index() {

        User user = User.find.byId(4L);

        if (user == null) {
            user = new User();
            user.setFirstname("Erik");
            user.setLastname("Wolf");
            Ebean.save(user);
        }

        user.setEmail("erik.wolf.29@gmail.com");
        user.setBirthday(Timestamp.valueOf(LocalDateTime.of(1986,10,29,0,0,0)));
        user.update();
       // Logger.info(user.firstname + " " + user.lastname + " " + user.email + " " + user.birthday.toLocalDateTime().toString());

        //Ebean.delete(user);
        //user.delete();

        return ok(index.render("Your new application is ready."));
    }

}
