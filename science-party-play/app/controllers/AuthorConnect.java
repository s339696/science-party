package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import exception.ac.AuthenticationException;
import manager.LoginManager;
import manager.PerkManager;
import models.ebean.Answer;
import models.ebean.Question;
import models.ebean.Topic;
import models.ebean.User;

import play.mvc.*;

import java.util.List;

/**
 * The AuthorConnect class is a interface to serve requested data via HTTP messages to the author tool application.
 * To get data from this interface, the request has to include the session which is provided by a /login request.
 * The logged in user has to be marked in the database as author.
 */
public class AuthorConnect extends Controller {

    /*
     * USER
     */

    /**
     * Returns the userdata for the user with the given Id as JSON.
     *
     * @param id
     * @return
     */
    public Result serveUser(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        User userServed = User.find.byId(id);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(userServed, JsonNode.class);

        return ok(node);
    }

    /**
     * Returns all Users as JSON.
     * @return
     */
    public Result serveUserList() {
        User user;
        try {
            user = isAuthorized();
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
     * @param
     * @return
     */
    public Result handleUserUpdate() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        // Generate userobject from json payload
        try {
            JsonNode json = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            User updateUser = mapper.convertValue(json, User.class);
            updateUser.update();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Der User wurde verändert.");
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
            return badRequest(e.getMessage());
        }

        // Generate userobject from json payload
        try {
            JsonNode json = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            User insertUser = mapper.convertValue(json, User.class);
            insertUser.setId(null);
            insertUser.insert();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Der User wurde hinzugefügt.");
    }

    public Result handleUserDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
            User.find.byId(id).delete();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Der User wurde gelöscht.");
    }

    /*
     * TOPIC
     */
    public Result serveTopic(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        Topic topicServed = Topic.find.byId(id);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(topicServed, JsonNode.class);

        return ok(node);
    }

    public Result serveTopicList() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        List<Topic> topiclist = Topic.find.all();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(topiclist, JsonNode.class);

        return ok(node);
    }

    public Result handleTopicUpdate() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        PerkManager.updatePerksPerTopic();

        return ok();
    }

    public Result handleTopicInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        PerkManager.updatePerksPerTopic();

        return ok();
    }

    public Result handleTopicDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
            Topic.find.byId(id).delete();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        PerkManager.updatePerksPerTopic();

        return ok("Der Topic wurde gelöscht.");
    }

    /*
     * QUESTION
     */
    public Result serveQuestion(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        Question questionServed = Question.find.byId(id);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(questionServed, JsonNode.class);

        return ok(node);
    }

    public Result serveQuestionList(Long topicId) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        List<Question> questionlist = Topic.find.byId(topicId).getQuestions();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(questionlist, JsonNode.class);

        return ok(node);
    }

    public Result handleQuestionUpdate() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleQuestionInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleQuestionDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
            Question.find.byId(id).delete();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Die Frage wurde gelöscht.");
    }

    /*
     * ANSWER
     */
    public Result serveAnswer(Long id) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        Answer answerServed = Answer.find.byId(id);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(answerServed, JsonNode.class);

        return ok(node);
    }

    public Result serveAnswerList(Long questionId) {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        List<Answer> answerlist = Question.find.byId(questionId).getAnswers();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        node = mapper.convertValue(answerlist, JsonNode.class);

        return ok(node);
    }

    public Result handleAnswerUpdate() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleAnswerInsert() {
        User user;
        try {
            user = isAuthorized();
        } catch (AuthenticationException e) {
            return badRequest(e.getMessage());
        }

        return ok();
    }

    public Result handleAnswerDelete(Long id) {
        User user;
        try {
            user = isAuthorized();
            Answer.find.byId(id).delete();
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        return ok("Die Antwort wurde gelöscht.");
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
