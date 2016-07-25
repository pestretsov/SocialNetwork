package utils;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by artemypestretsov on 7/25/16.
 */
public class ValidatorTest {
    @Test
    public void testUsernameOnlyWithLetters() throws Exception {
        String username = "ambush";
        assertTrue(Validator.validateUsername(username));
    }

    @Test
    public void testUsernameOnlyWithNumbers() throws Exception {
        String username = "1232123";
        assertTrue(Validator.validateUsername(username));
    }

    @Test
    public void testUsernameOf2Symbols() throws Exception {
        String username = "ss";
        assertFalse(Validator.validateUsername(username));
    }

    @Test
    public void testUsernameOf16Symbols() throws Exception {
        String username = "1234567890123456";
        assertFalse(Validator.validateUsername(username));
    }

    @Test
    public void testMixedUsernameOf15Symbols() throws Exception {
        String username = "12_12_SSSsZ0Amm";
        assertTrue(Validator.validateUsername(username));
    }
}
