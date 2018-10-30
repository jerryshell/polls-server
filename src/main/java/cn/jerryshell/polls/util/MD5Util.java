package cn.jerryshell.polls.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class MD5Util {
    private static String SALT = "";

    public static String MD5Base64(String str) {
        String result = null;
        str += SALT;
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            Base64.Encoder encoder = Base64.getEncoder();
            result = encoder.encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Autowired
    public void init(Environment environment) {
        MD5Util.SALT = environment.getProperty("md5util.salt");
    }
}
