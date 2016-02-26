package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exception.ac.AuthenticationException;
import models.ebean.User;

import models.form.LoginForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import java.util.List;

/**
 * The AuthorConnect class is a interface to serve requested data via HTTP messages to the administration application.
 * To get data from this interface, the request has to include the username and password of a user which is marked as author.
 * Request data as JSON (the password has to be a MD5 Hash):
 * {
 * "email":"value",
 * "password":"81DC9BDB52D04DC20036DBD8313ED055"
 * }
 *
 */
public class AuthorConnect extends Controller {

    /**
     * Returns the userdata for the user with the given ID as JSON.
     *
     * @param id
     * @return
     */
    public Result serveUser(Long id) {
        try {
            requestIsAuthenticated();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        User user = User.find.byId(id);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(user, JsonNode.class);

        return ok(node);
    }

    /**
     * Returns all Users as JSON.
     * @return
     */
    public Result serveUserlist() {
        try {
            requestIsAuthenticated();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        List<User> userlist = User.find.all();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(userlist, JsonNode.class);

        return ok(node);
    }

    /**
     * Private helper method to check if the request contains valid userdata from a autor.
     *
     * @return
     * @throws AuthenticationException
     */
    private boolean requestIsAuthenticated() throws AuthenticationException {
        Form<LoginForm> requestData = Form.form(LoginForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
           throw new AuthenticationException("Die Anfrage erfordert Username und Passwort.");
        } else {
            LoginForm loginForm = requestData.get();
            String email = loginForm.getEmail();
            String password = loginForm.getPassword();
            User user = User.find.where().like("email", email).findUnique();
            if (user == null) {
                throw new AuthenticationException("Der angegebene User ist nicht vorhanden.");
            }
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationException("Das Passwort für den angegeben User ist falsch.");
            }
            if (!user.isAuthor()) {
                throw new AuthenticationException("Der angegebene User ist kein Autor.");
            }
        }
        return true;
    }
}
