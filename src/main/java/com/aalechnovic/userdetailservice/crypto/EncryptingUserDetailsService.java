package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetails;
import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.domain.UserDetails;
import com.aalechnovic.userdetailservice.util.ObjectSerializer;
import com.aalechnovic.userdetailservice.util.Pair;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.util.Optional;

/**
 * Encrypting User Details Service which main purpose is to save provided {@link UserDetails} securely via encryption.
 * It relies on a single password for encryption (for simplicity) which (ideally) should be obtained remotely.
 *
 */
public class EncryptingUserDetailsService {

    private final EncryptedUserDetailsRepository encryptedUserDetailsRepository;
    private final ObjectSerializer objectSerializer;
    private final SecretKey secretKey;
    private final Encryptor encryptor;

    public EncryptingUserDetailsService(EncryptedUserDetailsRepository encryptedUserDetailsRepository,
                                        ObjectSerializer objectSerializer,
                                        Encryptor encryptor,
                                        Pair<String, byte[]> pwAndSalt) {
        this.encryptedUserDetailsRepository = encryptedUserDetailsRepository;
        this.objectSerializer = objectSerializer;
        this.encryptor = encryptor;
        this.secretKey = this.encryptor.getKeyFromPassword(pwAndSalt.getFirst(), pwAndSalt.getSecond());
    }

    /**
     * Saves userDetails in the database after the entire object is encrypted and serialized.
     *
     * @param userDetails to save
     * @return userDetails identifier and saved userDetails
     */
    public Pair<Long, UserDetails> save(UserDetails userDetails) {

        final var sealedObjAndRandBytes = encryptor.encryptObject(userDetails, secretKey);

        final var encryptedUserDetailsBytes = objectSerializer.serialize(sealedObjAndRandBytes.getFirst());

        final var encryptedUserDetails = new EncryptedUserDetails(sealedObjAndRandBytes.getSecond(), encryptedUserDetailsBytes);

        final var savedEncryptedUserDetails = encryptedUserDetailsRepository.save(encryptedUserDetails);

        final var savedUserDetails = decrypt(savedEncryptedUserDetails);

        return Pair.of(savedEncryptedUserDetails.getId(), savedUserDetails);
    }

    /**
     * Finds userDetails by id. Persistence entry is first deserialized, then decrypted before serving.
     *
     * @param id of the userDetails
     * @return userDetails identifier and saved userDetails
     */
    public Optional<Pair<Long, UserDetails>> findById(Long id){

        final var savedEncryptedUserDetails = encryptedUserDetailsRepository.findById(id);
        return savedEncryptedUserDetails.map(this::decrypt).map(userDetails -> Pair.of(id, userDetails));
    }

    private UserDetails decrypt(EncryptedUserDetails savedEncryptedUserDetails) {
        final var sealedEncryptedUserDetails = objectSerializer.deserialize(savedEncryptedUserDetails.getUserDetails(),
                                                                            SealedObject.class);

        return (UserDetails) encryptor.decryptObject(sealedEncryptedUserDetails,
                                                     secretKey,
                                                     savedEncryptedUserDetails.getRandomCryptBytes());
    }
}
