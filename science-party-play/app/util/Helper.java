package util;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {

    public static String getMD5fromString(String string) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return (new HexBinaryAdapter()).marshal(md5.digest(string.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
