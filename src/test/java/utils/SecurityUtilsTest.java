package utils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;

import static org.junit.Assert.*;

/**
 * Created by artemypestretsov on 7/21/16.
 */
public class SecurityUtilsTest {
    private SecurityUtils securityUtils = new SecurityUtils();

    @Test
    public void passwordIsGeneratedAndValidatedTest() throws Exception {
        String encrypted = securityUtils.encrypt("password");

        assertTrue(securityUtils.validatePassword("password", encrypted));
    }

    @Test
    public void samePasswordsDifferentHashesTest() throws Exception {
        String password = "1kejdfnb2h90";

        String hash1 = securityUtils.encrypt(password);
        String hash2 = securityUtils.encrypt(password);

        assertThat(hash1, is(not(hash2)));

        assertTrue(securityUtils.validatePassword(password, hash1));
        assertTrue(securityUtils.validatePassword(password, hash2));
    }

    @Test
    public void differentSaltsTest() throws Exception {
        String password = "password";

        String hash1 = securityUtils.encrypt(password);
        String hash2 = securityUtils.encrypt(password);

        String salt1 = hash1.substring(0, 32);
        String salt2 = hash2.substring(0, 32);

        assertThat(salt1, is(not(salt2)));
    }
}
