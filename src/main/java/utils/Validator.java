package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artemypestretsov on 7/25/16.
 */
public class Validator {

    // digits, underscore, letters (upper- and lowercase)
    private static final String usernamePatternString = "^\\w{3,15}$";
    private static final Pattern usernamePattern = Pattern.compile(usernamePatternString);

    public static boolean validateUsername(String username) {
        return username != null && usernamePattern.matcher(username).matches();
    }
}
