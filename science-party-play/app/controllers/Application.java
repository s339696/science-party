package controllers;

import com.avaje.ebean.Ebean;
import models.ebean.Perk;
import models.ebean.Topic;
import models.ebean.User;
import play.*;
import play.mvc.*;
import play.Logger;

import util.Helper;
import views.html.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Application extends Controller {

    public Result index() {

        return ok(index.render("Your new application is ready."));
    }

    public Result playground() {
        User user = User.find.byId(1L);

        Topic topic = new Topic();
        topic.setName("Forschungsmethoden");
        topic.insert();

        if (user == null) {
            user = new User();
            user.setFirstname("Erik");
            user.setLastname("Wolf");
            Ebean.save(user);
        }

        Perk perk = Perk.find.byId(1L);

        if (perk == null) {
            perk = new Perk();
            perk.setPerkName("Aussetzen");
            perk.setQrCode(Helper.getMD5fromString("Aussetzen"));
            Ebean.save(perk);
        }

        List<Perk> perks = user.getPerks();
        if (!perks.contains(perk)) {
            user.getPerks().add(perk);
        }

        user.update();

        //user.update();
        // Logger.info(user.firstname + " " + user.lastname + " " + user.email + " " + user.birthday.toLocalDateTime().toString());

        //Ebean.delete(user);
        //user.delete();
        return ok(playground.render("Let's Play!"));

    }

}
