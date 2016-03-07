package util;

import exception.UserException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides a nice range auf useful helpers like convertion methods.
 */
public class Helper {

    public static String getMD5fromString(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return (new HexBinaryAdapter()).marshal(md5.digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getStringFromTimestamp(Timestamp timestamp, String pattern) {

        LocalDateTime ldt = timestamp.toLocalDateTime();
        return ldt.toLocalDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Converts a dd.mm.yyyy String to a TimeStamp
     *
     * @param date
     * @return
     * @throws UserException
     */
    public static Timestamp getTimestampFromString(String date) {
        int day;
        int month;
        int year;

        if (!date.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            return null;
        }
        String[] splitedBirth = date.split("\\.");
        day = Integer.parseInt(splitedBirth[0]);
        month = Integer.parseInt(splitedBirth[1]);
        year = Integer.parseInt(splitedBirth[2]);
        Timestamp ts =  Timestamp.valueOf(LocalDateTime.of(year,month,day,0,0));
        return ts;
    }
}
