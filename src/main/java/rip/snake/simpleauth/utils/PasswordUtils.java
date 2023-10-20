package rip.snake.simpleauth.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtils {

    public static boolean isPasswordValid(String password, String hashed) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashed.toCharArray()).verified;
    }

    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

}
