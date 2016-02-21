package controllers;


import exception.LoginException;
import manager.LoginManager;
import manager.UserManager;
import models.ebean.User;
import models.form.LoginForm;
import models.form.RegisterForm;
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
        if (LoginManager.isLoggedIn()) {
            return redirect(controllers.routes.Application.renderHome());
        }
        return ok(views.html.login.render("Anmeldung"));
    }

    /**
     * Handles a login request.
     *
     * Required JSON Data:
     *
     * {
     * "email":"value",
     * "password":"value"
     * }
     *
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
            String email = loginForm.getEmail();
            String password = loginForm.getPassword();

            try {
                User user = LoginManager.login(email,password);
                return ok("Hallo " + user.getFirstname() + ", du wurdest erfolgreich eingeloggt.");
            } catch (LoginException e) {
                return badRequest(e.getMessage());
            }
        }
    }

    /**
     * Handles a register request and creates in the end a new user.
     *
     * Required JSON Data:
     *
     * {
     * "firstname":"Max",
     * "lastname":"Mustermann",
     * "birthday":"01.01.1980",
     * "email":"max@mustermann.de",
     * "password":"1234"
     * }
     *
     * @return
     */
    public Result handleRegisterRequest() {
        if (LoginManager.isLoggedIn()) {
            return badRequest("Es ist bereits ein User eingeloggt.");
        }

        Form<RegisterForm> requestData = Form.form(RegisterForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        } else {
            RegisterForm regForm = requestData.get();
            try {
                User user = UserManager.createUser(regForm.getFirstname(),regForm.getLastname(),regForm.getBirthday(),regForm.getEmail(),regForm.getPassword());
                user = LoginManager.login(regForm.getEmail(), regForm.getPassword());

                return ok("Der Account wurde erfolgreich erstellt und du bist jetzt eingeloggt.");
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
        }
    }
}
