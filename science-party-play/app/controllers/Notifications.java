package controllers;

import com.avaje.ebean.Ebean;
import manager.LoginManager;
import models.ebean.Message;
import models.ebean.Notification;
import models.ebean.User;
import net.sf.ehcache.search.expression.Not;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * This controller handles all requests related to notifications
 */
public class Notifications extends Controller {

    public Result renderPersonalNotifications() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<Notification> unseenNoti = Notification.getUnseenNotifications(user);
        List<Notification> seenNoti = Notification.getSeenNotifications(user);

        Ebean.beginTransaction();
        try {
            for (Notification noti: unseenNoti) {
                noti.setPrivateSeen(true);
                noti.update();
            }
            Ebean.commitTransaction();
        } finally {
            Ebean.endTransaction();
        }

        return ok(views.html.notifications.notifications.render(user, unseenNoti, seenNoti));
    }

    public Result renderPublicNotifications() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return redirect(controllers.routes.Public.renderLoginPage());
        }

        List<Notification> noti = null;

        return ok(views.html.notifications.newsfeed.render(user, noti));
    }

    public Result handlePersonalNotificationCount() {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            return badRequest("");
        }

        int count = user.getUnseenNotificationsCount();

        return ok(String.valueOf(count));
    }

}
