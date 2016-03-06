package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exception.ac.AuthenticationException;
import manager.LoginManager;
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
    /*
     * USER
     */

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
    public Result serveUserList() {
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
     * Updates the data of a user in the database.
     *
     * @param id
     * @return
     */
    public Result handleUserUpdate(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    /**
     * Inserts a new user into the database.
     *
     * @return
     */
    public Result handleUserInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleUserDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    /*
     * TOPIC
     */
    public Result serveTopic(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result serveTopicList() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleTopicUpdate(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleTopicInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleTopicDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    /*
     * QUESTION
     */
    public Result serveQuestion(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result serveQuestionList(Long topicId) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleQuestionUpdate(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleQuestionInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleQuestionDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    /*
     * ANSWER
     */
    public Result serveAnswer(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result serveAnswerList(Long questionId) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleAnswerUpdate(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleAnswerInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleAnswerDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            badRequest(e.getMessage());
        }

        return ok();
    }

    /**
     * Private helper method to check if the request contains valid userdata from a autor.
     *
     * @return
     * @throws AuthenticationException
     */

    /* FIXME: DEPRACTED */
    private boolean requestIsAuthenticated() throws AuthenticationException {
        Form<LoginForm> requestData = Form.form(LoginForm.class).bindFromRequest();
        if (requestData.hasErrors()) {
           throw new AuthenticationException("Die Anfrage erfordert E-Mail und Passwort.");
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

    /**
     * Checks if a user is logged in and authorized to change data.
     *
     * @return
     */
    private User isAuthorized() throws AuthenticationException {
        User user = LoginManager.getLoggedInUser();
        if (user == null) {
            throw new AuthenticationException("Es ist kein User eingeloggt.");
        } else {
            if (!user.isAuthor()) {
                throw new AuthenticationException("Der eingeloggte User ist kein Autor.");
            }
        }
        return user;
    }
}
