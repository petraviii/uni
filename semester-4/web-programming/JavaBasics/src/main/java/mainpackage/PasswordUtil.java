package mainpackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for securely hashing and verifying passwords using
 * SHA-256 with a per-user random salt.
 *
 * Storage format: each user row stores two separate columns —
 * "salt" (random, unique per user) and "password_hash" (the SHA-256
 * hash of salt + plaintext password, Base64-encoded for storage as text).
 *
 * @author Petros Travlos P21173
 */
public class PasswordUtil {

    /**
     * Generates a new random salt, suitable for storing alongside a
     * user's password hash. Each user should get their own unique salt.
     *
     * @return a Base64-encoded random salt string
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 16 bytes = 128 bits, standard salt size
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    /**
     * Hashes a plaintext password combined with a given salt using SHA-256.
     *
     * @param password  the plaintext password to hash
     * @param salt      the salt to combine with the password (Base64-encoded)
     * @return the resulting hash, Base64-encoded for storage as text
     */
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.getDecoder().decode(salt));
            byte[] hashedBytes = digest.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            // SHA-256 and UTF-8 are always available on any standard JVM,
            // so this should never actually happen — wrapping as unchecked
            // avoids forcing every caller to handle an impossible case.
            throw new RuntimeException("Unexpected error while hashing password", e);
        }
    }

    /**
     * Verifies a plaintext password attempt against a stored salt and hash.
     *
     * @param passwordAttempt the plaintext password the user typed in
     * @param salt             the stored salt for this user
     * @param storedHash       the stored hash for this user
     * @return true if the password attempt is correct, false otherwise
     */
    public static boolean verifyPassword(String passwordAttempt, String salt, String storedHash) {
        String attemptHash = hashPassword(passwordAttempt, salt);
        return attemptHash.equals(storedHash);
    }
}