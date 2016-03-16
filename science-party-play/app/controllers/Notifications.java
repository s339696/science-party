package controllers;

import manager.LoginManager;
import models.ebean.Notification;
import models.ebean.User;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller handles all requests related to notifications
 */
public class Notifications extends Controller {

    public Result renderPersonalNotifications() {
        return ok();
    }

    public Result renderPublicNotifications() {
        return ok();
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
