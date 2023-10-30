package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.util.Pair;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.io.Serializable;

public interface Encryptor {
    SecretKey getKeyFromPassword(String password, byte[] salt);

    Pair<SealedObject, byte[]> encryptObject(Serializable object, SecretKey key);

    Serializable decryptObject(SealedObject sealedObject, SecretKey key, byte[] randomBytes);
}
