package cn.jerryshell.polls.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {
    private static Algorithm ALGORITHM;

    public static String sign(String username, Date expiresAt, String role) {
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(expiresAt)
                .withClaim("role", role)
                .sign(ALGORITHM);
    }

    public static boolean verify(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }
        try {
            JWT.require(ALGORITHM).build().verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public static String getUsername(String token) {
        try {
            return JWT.decode(token).getClaim("username").asString();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    public static String getRole(String token) {
        try {
            return JWT.decode(token).getClaim("role").asString();
        } catch (JWTDecodeException exception) {
            return null;
        }
    }

    @Autowired
    public void init(Environment environment) {
        JWTUtil.ALGORITHM = Algorithm.HMAC256(Objects.requireNonNull(environment.getProperty("jwt.secret")));
    }
}
