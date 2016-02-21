package util;

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
}
