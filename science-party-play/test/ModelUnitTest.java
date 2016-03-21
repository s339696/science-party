import java.io.File;
import java.util.HashMap;
import java.util.Map;


import com.avaje.ebean.Ebean;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import manager.PerkManager;
import models.ebean.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import play.Configuration;
import play.db.DB;
import play.db.Database;
import play.db.evolutions.Evolution;
import play.db.evolutions.Evolutions;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 *
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelUnitTest {

    public static FakeApplication app;

    @BeforeClass
    public static void startApp() {
        // Load test config  with testserver
        Map<String, String> settings = new HashMap<String, String>();
        settings.put("db.default.driver", "com.mysql.jdbc.Driver");
        settings.put("db.default.url", "jdbc:mysql://win9188.informatik.uni-wuerzburg.de:3306/sopra-ws1516-team10-test?autoReconnect=true&useSSL=false");
        settings.put("db.default.username", "root");
        settings.put("db.default.password", "root");
        settings.put("play.evolutions.autoApply", "true");
        settings.put("ebean.default", "models.ebean.*");
        app = play.test.Helpers.fakeApplication(settings);

        //Evolutions.applyEvolutions(XXXXXX);

        Helpers.start(app);
    }


    /**
     * Tests if it's possible to create a user.
     */
    @Test
    public void a_testCreateUser() {
        try {
            User.createUser("Max", "Mustermann", "01.01.1980", "max.mustermann@gmail.com", "1234");
            User testUser = User.find.where()
                    .eq("email", "max.mustermann@gmail.com")
                    .findUnique();
            assertNotNull(testUser);
            assertNotNull(testUser);
            assertEquals(testUser.getFirstname(), "Max");
            assertEquals(testUser.getLastname(), "Mustermann");
            assertEquals(testUser.getBirthdayAsString(), "01.01.1980");
            assertEquals(testUser.getEmail(), "max.mustermann@gmail.com");
            assertEquals(testUser.getPassword(), "81DC9BDB52D04DC20036DBD8313ED055");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Tests if a user is updateable
     */
    @Test
    public void b_testUpdateUser() {
        try {
            User testUser = User.find.where()
                    .eq("email", "max.mustermann@gmail.com")
                    .findUnique();
            assertNotNull(testUser);
            testUser.setPoints(20);
            testUser.update();
            assertEquals(User.find.where()
                    .eq("email", "max.mustermann@gmail.com")
                    .findUnique().getPoints(), 20);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Tests to create a topic
     */
    @Test
    public void a_createTopic() {
        try {
            Topic testTopic = new Topic();
            testTopic.setName("Testtopic");
            testTopic.save();
            assertNotNull(Topic.find.where()
                    .eq("name", "Testtopic")
                    .findUnique());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Test to create a question
     */
    @Test
    public void b_createQuestions() {
        try {
            Topic testTopic = Topic.find.where()
                    .eq("name", "Testtopic")
                    .findUnique();
            assertNotNull(testTopic);

            Question q1 = new Question();
            q1.setTopic(testTopic);
            q1.setDifficulty(10);
            q1.setText("Frage 1");
            q1.save();
            Question q2 = new Question();
            q2.setTopicId(testTopic.getId());
            q2.setDifficulty(10);
            q2.setText("Frage 2");
            q2.save();
            assertNotNull(Question.find.where()
                    .eq("text", "Frage 1")
                    .findUnique());
            assertNotNull(Question.find.where()
                    .eq("text", "Frage 2")
                    .findUnique());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Test to create answers
     */
    @Test
    public void c_createAnswers() {
        try {
            Question q1 = Question.find.where()
                    .eq("text", "Frage 1")
                    .findUnique();
            Question q2 = Question.find.where()
                    .eq("text", "Frage 2")
                    .findUnique();

            for (int i = 0; i < 4; i++) {
                Answer answer = new Answer();
                answer.setQuestion(q1);
                answer.setText("Antwort " + i);
                if (i == 1) {
                    answer.setCorrect(true);
                }
                answer.save();

                assertNotNull(Answer.find.where()
                        .eq("question", q1)
                        .eq("text", "Antwort " + i)
                        .findUnique());
            }

            for (int i = 0; i < 4; i++) {
                Answer answer = new Answer();
                answer.setQuestionId(q2.getId());
                answer.setText("Antwort " + i);
                if (i == 2) {
                    answer.setCorrect(true);
                }
                answer.save();

                assertNotNull(Answer.find.where()
                        .eq("question", q2)
                        .eq("text", "Antwort " + i)
                        .findUnique());
            }

        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void a_createPerk() {
        try {
            Perk testPerk = new Perk();
            testPerk.setName("Frage automatisch richtig beantworten");
            testPerk.insert();
            assertNotNull(Perk.find.where()
                    .eq("name", "Frage automatisch richtig beantworten")
                    .findUnique());
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void g_createQrCodes() {
        try {
            PerkManager.updatePerksPerTopic();
            PerkPerTopic testPerkPerTopic = PerkPerTopic.find.where()
                    .eq("perk.name", "Frage automatisch richtig beantworten")
                    .eq("topic.name", "Testtopic")
                    .findUnique();
            assertNotNull(testPerkPerTopic);
            assertEquals(testPerkPerTopic.getQrCode(), "78714A151C52864E0B607091BA1E112E");
        } catch (Exception e) {
            assertNull(e);
        }

    }

    @Test
    public void m_createSinglePlayerGame() {

    }


    @Test
    public void n_playSinglePlayerGame() {

    }

    /**
     * Deletes a topic and tests if questions and answers are gone
     */
    @Test
    public void y_deleteTopicAndRelations() {
        try {
            Topic testTopic = Topic.find.where()
                    .eq("name", "Testtopic")
                    .findUnique();
            assertNotNull(testTopic);
            testTopic.delete();
            assertEquals(Topic.find.all().size(), 0);
            assertEquals(Question.find.all().size(), 0);
            assertEquals(Answer.find.all().size(), 0);
            assertEquals(PerkPerTopic.find.all().size(), 0);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void z_deletePerk() {
        try {
            Perk testPerk = Perk.find.where()
                    .eq("name", "Frage automatisch richtig beantworten")
                    .findUnique();
            testPerk.delete();
            assertEquals(Perk.find.all().size(), 0);
        } catch (Exception e) {
            assertNull(e);
        }
    }

    /**
     * Tests if it's possible to delete a user.
     */
    @Test
    public void z_testDeleteUser() {
        try {
            User testUser = User.find.where()
                    .eq("email", "max.mustermann@gmail.com")
                    .findUnique();
            assertNotNull(testUser);
            testUser.delete();
            testUser = User.find.where()
                    .eq("email", "max.mustermann@gmail.com")
                    .findUnique();
            assertNull(testUser);
        } catch (Exception e) {
            assertNull(e);
        }
    }


    @AfterClass
    public static void stopApp() {
        //Evolutions.cleanupEvolutions(XXXXXX);
        Helpers.stop(app);
    }

}
