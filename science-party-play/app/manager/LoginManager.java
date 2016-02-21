package manager;

import exception.LoginException;
import models.ebean.User;
import play.Logger;
import util.Helper;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static play.mvc.Controller.session;

/**
 * The LoginManger handles Logins and the Usersession.
 */
public class LoginManager {

    /**
     * Creates a Usersession if the Login Data match a User.
     * @param email
     * @param password
     * @return
     */
    public static User login(String email, String password) throws LoginException {
        password = Helper.getMD5fromString(password);
        Logger.info(password);
        User user = User.find.where().ilike("email",email).findUnique();
        if (user != null) {
            if (user.getPassword().equals(password)) {
                session("userid", user.getId().toString());
                session("email", email);
                session("password", password);
                updateLastOnline(user);
                return user;
            } else {
                throw new LoginException("Das eingegebene Passwort war falsch.");
            }
        } else {
            throw new LoginException("Es wurde kein Account mit der eingegebenen Mailadresse gefunden.");
        }
    }

    /**
     * Ends a Usersession
     */
    public static void logout() {
        session().clear();
    }

    /**
     * Returns whether a User is logged in or not.
     * @return
     */
    public static boolean isLoggedIn() {
        if (getLoggedInUser() == null) {
            return false;
        }
        return true;
    }

    /**
     * Returns the logged in User or null of no User is logged in.
     * @return
     */
    public static User getLoggedInUser() {
        String email = session("email");
        String password = session("password");
        if (email == null) {
            return null;
        } else {
            // Test if the password has changed since last Login and logout() if it has changed
            User user = User.find.where().ilike("email",email).ilike("password",password).findUnique();
            if (user == null) {
                logout();
            }
            updateLastOnline(user);
            return user;
        }
    }

    /**
     * Updates last login time.
     * @param user
     */
    private static void updateLastOnline(User user) {
        user.setLastOnline(Timestamp.valueOf(LocalDateTime.now()));
        user.update();
    }
}
