package controllers;

import exception.LoginException;
import manager.LoginManager;
import models.ebean.User;
import models.form.LoginForm;
import play.mvc.*;
import play.data.Form;


/**
 * The public class handles all requests for the public area.
 */
public class Public extends Controller {

    /**
     * Renders the login page.
     * @return
     */
    public Result renderLoginPage() {
        return ok(views.html.login.render("Anmeldung"));
    }

    /**
     * Handles a login request.
     * @return
     */
    public Result handleLoginRequest() {
        if (LoginManager.isLoggedIn()) {
            return badRequest("Es ist bereits ein User eingeloggt.");
        }

        Form<LoginForm> requestData = Form.form(LoginForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        } else {
            LoginForm loginForm = requestData.get();
            String username = loginForm.getEmail();
            String password = loginForm.getPassword();

            try {
                User user = LoginManager.login(username,password);
                return ok("Hallo " + user.getFirstname() + ", du wurdest erfolgreich eingeloggt.");
            } catch (LoginException e) {
                return badRequest(e.getMessage());
            }
        }
    }

}
