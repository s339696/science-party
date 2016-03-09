package controllers;


import exception.LoginException;
import manager.LoginManager;
import models.ebean.User;
import models.form.LoginForm;
import models.form.UserAccountForm;
import play.mvc.*;
import play.data.Form;


/**
 * The publ class handles all requests for the publ area.
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
        return ok(views.html.publ.login.render());
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
     * Renders the register page.
     * @return
     */
    public Result renderRegister() {
        if (LoginManager.isLoggedIn()) {
            return redirect(controllers.routes.Application.renderHome());
        }
        return ok(views.html.publ.register.render());
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

        Form<UserAccountForm> requestData = Form.form(UserAccountForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
            return badRequest("Es wurden nicht alle benötigten Felder ausgefüllt.");
        } else {
            UserAccountForm regForm = requestData.get();
            try {
                User user = User.createUser(regForm.getFirstname(),regForm.getLastname(),
                        regForm.getBirthday(),regForm.getEmail(),regForm.getPassword());
                user = LoginManager.login(regForm.getEmail(), regForm.getPassword());

                return ok("Der Account wurde erfolgreich erstellt und du bist jetzt eingeloggt.");
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }
        }
    }

    public Result renderNews() {
        return ok(views.html.publ.news.render());
    }

    public Result renderBlog() {
        return ok(views.html.publ.blog.render());
    }

    public Result renderEvents() {
        return ok(views.html.publ.events.render());
    }

    public Result renderKontakt() {
        return ok(views.html.publ.kontakt.render());
    }
}
