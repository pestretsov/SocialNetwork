package utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by artemypestretsov on 7/21/16.
 */
public class SecurityUtils {
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH = 32;

    public String encrypt(String input) {
        try {
            String salt = generateSalt();
            System.out.println(salt);
            String saltedInputHash = hash(salt+input);
            return salt+saltedInputHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validatePassword(String password, String fullHash) {
        try {
            String salt = fullHash.substring(0, SALT_LENGTH);
            String saltedPasswordHash = fullHash.substring(SALT_LENGTH);
            System.out.println(salt);
            System.out.println(salt + hash(salt + password));
            return saltedPasswordHash.equals(hash(salt + password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] saltBytes = new byte[SALT_LENGTH/2];
        sr.nextBytes(saltBytes);
        return GeneralUtils.bytesToHexString(saltBytes);
    }

    private String hash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASHING_ALGORITHM);
        md.update(input.getBytes(), 0, input.length());

        return new BigInteger(1, md.digest()).toString(16);
    }
}

