package com.aalechnovic.userdetailservice.persistence;

import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetails;
import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.persistence.config.DataJpaMySQLContainerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaMySQLContainerTest
class EncryptedUserDetailsRepositoryTest {

    @Autowired
    private EncryptedUserDetailsRepository encryptedUserDetailsRepository;

    @Test
    public void saveNewUserDetails_OK_IdAllocated(){
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);

        EncryptedUserDetails encryptedUserDetails = new EncryptedUserDetails(null, randomBytes, randomBytes);

        EncryptedUserDetails savedDetails = encryptedUserDetailsRepository.save(encryptedUserDetails);

        assertThat(savedDetails).isNotNull();
        assertThat(savedDetails.getId()).isNotNull();
        assertThat(savedDetails.getUserDetails()).isEqualTo(randomBytes);
        assertThat(savedDetails.getRandomCryptBytes()).isEqualTo(randomBytes);
    }

    @Test
    public void overwriteUserDetails_OK(){
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);

        EncryptedUserDetails encryptedUserDetails = new EncryptedUserDetails(null,randomBytes, randomBytes);

        EncryptedUserDetails savedDetails = encryptedUserDetailsRepository.save(encryptedUserDetails);

        assertThat(savedDetails).isNotNull();
        assertThat(savedDetails.getId()).isNotNull();
        assertThat(savedDetails.getUserDetails()).isEqualTo(randomBytes);
        assertThat(savedDetails.getRandomCryptBytes()).isEqualTo(randomBytes);

        byte[] randomOtherBytes = new byte[16];
        new SecureRandom().nextBytes(randomOtherBytes);

        EncryptedUserDetails encryptedUserDetailsToOverwrite = new EncryptedUserDetails(savedDetails.getId(), randomOtherBytes, randomOtherBytes);

        EncryptedUserDetails overwrittenSavedUserDetails = encryptedUserDetailsRepository.save(encryptedUserDetailsToOverwrite);

        assertThat(overwrittenSavedUserDetails).isNotNull();
        assertThat(overwrittenSavedUserDetails.getId()).isNotNull().isEqualTo(savedDetails.getId());
        assertThat(overwrittenSavedUserDetails.getUserDetails()).isEqualTo(randomOtherBytes);
        assertThat(overwrittenSavedUserDetails.getRandomCryptBytes()).isEqualTo(randomOtherBytes);

    }
}