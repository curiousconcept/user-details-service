package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.util.ObjectSerializer;
import com.aalechnovic.userdetailservice.util.Pair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class EncryptingUserDetailsServiceTest {

    @Mock
    private EncryptedUserDetailsRepository encryptedUserDetailsRepository;

    private EncryptingUserDetailsService encryptingUserDetailsService;

    @BeforeEach
    void setUp() {
        encryptingUserDetailsService = new EncryptingUserDetailsService(encryptedUserDetailsRepository,
                                                                        new ObjectSerializer(new ObjectMapper()), Pair.of("pass",new byte[] {-128,20,30,127}));
    }

    @Test
    void save() {
    }

    @Test
    void findById() {
    }
}