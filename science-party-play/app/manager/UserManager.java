package manager;

import exception.UserException;
import models.ebean.User;
import util.Helper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * The UserManager provides useful methods around the Use.
 */
public class UserManager {

    /**
     * Creates a new User in Database with given parameters.
     * @param firstname
     * @param lastname
     * @param birthday
     * @param email
     * @param password
     * @return
     * @throws UserException
     */
    public static User createUser(String firstname, String lastname, String birthday, String email, String password) throws UserException {
        User user = new User();

        /* TODO: Check data for validity */
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setBirthday(checkAndConvertBirthday(birthday));
        user.setEmail(checkEmail(email));
        user.setPassword(Helper.getMD5fromString(password));
        user.setLocked(false);
        user.setPoints(0);

        // Insert in Database
        try {
            user.insert();
        }catch (Exception e) {
            throw new UserException("Es konnte leider kein neuer User erstellt werden.");
        }

        return user;
    }

    /**
     * Check format of a given birthday string and converts to Timestamp.
     * @param birthday
     * @return
     * @throws UserException
     */
    private static Timestamp checkAndConvertBirthday(String birthday) throws UserException {
        int day;
        int month;
        int year;

        if (!birthday.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new UserException("Der angegebene Geburtstag hat nicht das richtige Format (dd.mm.yyyy).");
        }
        String[] splitedBirth = birthday.split("\\.");
        day = Integer.parseInt(splitedBirth[0]);
        month = Integer.parseInt(splitedBirth[0]);
        year = Integer.parseInt(splitedBirth[0]);

        return Timestamp.valueOf(LocalDateTime.of(year,month,day,0,0));
    }

    private static String checkEmail(String email) throws UserException {
        //Check if email address already exist in DB.
        if (User.find.where().like("email",email).findUnique() != null) {
            throw new UserException("Es ist bereits ein User mit dieser Mailadresse vorhanden.");
        }
        //TODO: Check for format
        return email;
    }
}
