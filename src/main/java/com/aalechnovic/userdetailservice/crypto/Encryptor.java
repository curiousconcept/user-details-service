package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.util.Pair;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * AES Encryptor with initialization vector using native Cipher
 * Adapted from <a href="https://www.baeldung.com/java-aes-encryption-decryption"> Baeldung </a>.
 * Jasypt wasn't used because the last published version was in 2019. It doesn't have support for hibernate 6 hence yields no benefits for the context.
 *
 */
public class Encryptor {

    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int SECRET_KEY_ITERATION_COUNT = 65536;
    private static final int SECRET_KEY_LENGTH = 256;
    private static final String SECRET_KEY_GEN_ALGO = "PBKDF2WithHmacSHA256";
    private static final String SECRET_KEY_SPEC_ALGO = "AES";

    /**
     *
     * Obtain secret key from the password and salt, ensure salt is not obtained from {@link String#getBytes()} as the result
     * can vary by platform encoding.
     *
     * @param password secret password to use for the key
     * @param salt salt to mix with password
     * @return {@link javax.crypto.SecretKey}
     */
    public static SecretKey getKeyFromPassword(String password, byte[] salt) {

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_GEN_ALGO);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, SECRET_KEY_ITERATION_COUNT, SECRET_KEY_LENGTH);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), SECRET_KEY_SPEC_ALGO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * Encrypt serializable object using a secret key
     *
     * @param object a Serializable object to encrypt
     * @param key {@link javax.crypto.SecretKey} to use for encryption
     * @return Pair of {@link javax.crypto.SealedObject} and random bytes as initialization vector. The bytes must be saved alongside the sealed object
     * for further decryption.
     */
    public static Pair<SealedObject, byte[]> encryptObject(Serializable object, SecretKey key) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            byte[] initializationVector = generateRandomBytes();
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initializationVector));
            return Pair.of(new SealedObject(object, cipher), initializationVector) ;

        } catch (Exception cryptoEx) {
            throw new RuntimeException(cryptoEx);
        }
    }

    /**
     *
     * Decrypt sealed using secret key and previously saved random bytes used during encryption
     *
     * @param sealedObject an {@link javax.crypto.SealedObject} to decrypt
     * @param key {@link javax.crypto.SecretKey} to use for decryption
     * @return Decrypted serializable object
     */
    public static Serializable decryptObject(SealedObject sealedObject, SecretKey key, byte[] randomBytes)  {
        try {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(randomBytes));
        return (Serializable) sealedObject.getObject(cipher);
        } catch (Exception cryptoEx) {
            throw new RuntimeException(cryptoEx);
        }
    }

    private static byte[] generateRandomBytes() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

}