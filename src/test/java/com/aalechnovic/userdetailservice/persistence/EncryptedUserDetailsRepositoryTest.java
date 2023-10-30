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

        EncryptedUserDetails encryptedUserDetails = new EncryptedUserDetails(randomBytes, randomBytes);

        EncryptedUserDetails savedDetails = encryptedUserDetailsRepository.save(encryptedUserDetails);

        assertThat(savedDetails).isNotNull();
        assertThat(savedDetails.getId()).isNotNull();
        assertThat(savedDetails.getUserDetails()).isEqualTo(randomBytes);
        assertThat(savedDetails.getRandomCryptBytes()).isEqualTo(randomBytes);
    }

    @Test
    public void savedUserDetailsCanBeFound(){
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);

        EncryptedUserDetails encryptedUserDetails = new EncryptedUserDetails(randomBytes, randomBytes);

        EncryptedUserDetails savedDetails = encryptedUserDetailsRepository.save(encryptedUserDetails);

        assertThat(savedDetails).isNotNull();
        assertThat(savedDetails.getId()).isNotNull();

        var foundUserDetails = encryptedUserDetailsRepository.findById(savedDetails.getId());

        assertThat(foundUserDetails.isPresent()).isTrue();
        assertThat(foundUserDetails.get().getId()).isEqualTo(savedDetails.getId());
    }
}