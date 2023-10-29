package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.StreamCorruptedException;
import java.security.SecureRandom;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EncryptorTest {

    @Test
    void givenStringObj_whenEncryptValid_thenSuccess() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        SecretKey key = Encryptor.getKeyFromPassword("test", salt);

        String obj = "TestObject";

        Pair<SealedObject, byte[]> encryptionRes = Encryptor.encryptObject(obj, key);

        String plainText = (String) Encryptor.decryptObject(encryptionRes.getFirst(), key, encryptionRes.getSecond());

        Assertions.assertEquals(obj, plainText);
    }

    @Test
    void givenStringObj_whenEncryptWrongSaltPw_thenFailure() {
        // given

        byte[] salt = new byte[16];
        new Random().nextBytes(salt);

        SecretKey key = Encryptor.getKeyFromPassword("test", salt);

        String obj = "TestObject";

        // when
        Pair<SealedObject, byte[]> encryptionRes = Encryptor.encryptObject(obj, key);

        new Random().nextBytes(salt);

        SecretKey keyWithWrongSalt = Encryptor.getKeyFromPassword("test", salt);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            Encryptor.decryptObject(encryptionRes.getFirst(), keyWithWrongSalt, encryptionRes.getSecond());
        });


        String expectedMessage = "Given final block not properly padded. Such issues can arise if a bad key is used during decryption";
        String actualMessage = exception.getMessage();

        assertThat(exception.getCause()).isInstanceOf(BadPaddingException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenStringObj_whenEncryptWrongPw_thenFailure() {

        byte[] salt = new byte[16];
        new Random().nextBytes(salt);

        SecretKey key = Encryptor.getKeyFromPassword("test", salt);

        String obj = "TestObject";

        Pair<SealedObject, byte[]> encryptionRes = Encryptor.encryptObject(obj, key);

        SecretKey keyWithWrongPw = Encryptor.getKeyFromPassword("test2", salt);

        Exception exception = assertThrows(RuntimeException.class, () ->
                Encryptor.decryptObject(encryptionRes.getFirst(), keyWithWrongPw, encryptionRes.getSecond()));

        String expectedMessage = "Given final block not properly padded. Such issues can arise if a bad key is used during decryption";
        String actualMessage = exception.getMessage();

        assertThat(exception.getCause()).isInstanceOf(BadPaddingException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void givenStringObj_whenEncryptWrongInitializationVector_thenFailure() {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        SecretKey key = Encryptor.getKeyFromPassword("test", salt);

        String obj = "TestObject";

        Pair<SealedObject, byte[]> encryptionRes = Encryptor.encryptObject(obj, key);

        Exception exception = assertThrows(RuntimeException.class, () ->  Encryptor.decryptObject(encryptionRes.getFirst(), key, salt));

        String expectedMessage = "invalid stream header";
        String actualMessage = exception.getMessage();

        assertThat(exception.getCause()).isInstanceOf(StreamCorruptedException.class);
        assertTrue(actualMessage.contains(expectedMessage));
    }
}