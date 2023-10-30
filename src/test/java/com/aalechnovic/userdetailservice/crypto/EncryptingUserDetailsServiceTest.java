package com.aalechnovic.userdetailservice.crypto;

import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetails;
import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.domain.UserDetails;
import com.aalechnovic.userdetailservice.util.ObjectSerializer;
import com.aalechnovic.userdetailservice.util.ObjectSerializerImpl;
import com.aalechnovic.userdetailservice.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EncryptingUserDetailsServiceTest {

    @Mock
    private EncryptedUserDetailsRepository encryptedUserDetailsRepository;

    @Spy
    private final ObjectSerializer objectSerializer = new ObjectSerializerImpl();

    @Spy
    private final Encryptor encryptor = new EncryptorImpl();

    private EncryptingUserDetailsService encryptingUserDetailsService;

    @BeforeEach
    void setUp() {
        this.encryptingUserDetailsService = new EncryptingUserDetailsService(encryptedUserDetailsRepository,
                                                                             objectSerializer,
                                                                             encryptor,
                                                                             Pair.of("pass", new byte[]{-128, 20, 30, 127}));
    }

    @Test
    void saveUserDetails_OK() {

        UserDetails userDetails = new UserDetails("test@test.test", "testname", "testsurname");
        setupRepoToReturnWhatWasPassed();

        Pair<Long, UserDetails> userIdAndDetailsPair = this.encryptingUserDetailsService.save(userDetails);

        assertThat(userIdAndDetailsPair).isNotNull();
        assertThat(userIdAndDetailsPair.getFirst()).isNotNull();
        assertThat(userIdAndDetailsPair.getSecond()).isNotNull();
        assertThat(userIdAndDetailsPair.getSecond().email()).isEqualTo(userDetails.email());
        assertThat(userIdAndDetailsPair.getSecond().surname()).isEqualTo(userDetails.surname());
        assertThat(userIdAndDetailsPair.getSecond().name()).isEqualTo(userDetails.name());
    }

    /*
        Bypasses actual encryption via spies. Verify that the objects are interacted with.
     */
    @Test
    void findById_OK() {

        UserDetails userDetails = new UserDetails("test@test.test", "testname", "testsurname");

        EncryptedUserDetails encryptedUserDetails = new EncryptedUserDetails(new byte[5], new byte[1]);

        doReturn(Optional.of(encryptedUserDetails)).when(encryptedUserDetailsRepository).findById(1L);

        SealedObject sealedObject = mock(SealedObject.class);

        doReturn(sealedObject).when(objectSerializer).deserialize(new byte[1], SealedObject.class);
        doReturn(userDetails).when(encryptor).decryptObject(eq(sealedObject), any(SecretKey.class), eq(new byte[5]));

        Optional<Pair<Long, UserDetails>> userIdAndDetailsPair = this.encryptingUserDetailsService.findById(1L);

        assertThat(userIdAndDetailsPair.isPresent()).isTrue();

        assertThat(userIdAndDetailsPair.get().getFirst()).isNotNull().isEqualTo(1L);
        assertThat(userIdAndDetailsPair.get().getSecond()).isNotNull();
        assertThat(userIdAndDetailsPair.get().getSecond().name()).isEqualTo("testname");

    }

    /*
        Returns the encrypted user details as passed. If an ID is provided - returns the id otherwise generates new id, like
        the real repo would.
     */
    private void setupRepoToReturnWhatWasPassed() {
        doAnswer(((Answer<EncryptedUserDetails>) invocation -> {
            var castInvocation = (EncryptedUserDetails)  invocation.getArgument(0);
            Long id;

            if(castInvocation.getId()==null)
                id = new Random().nextLong();
            else
                id = castInvocation.getId();

            return new TestEncryptedUserDetails(id, castInvocation.getRandomCryptBytes(), castInvocation.getUserDetails());
        })).when(encryptedUserDetailsRepository).save(any(EncryptedUserDetails.class));
    }

    static class TestEncryptedUserDetails extends EncryptedUserDetails{
        protected TestEncryptedUserDetails(Long id, byte[] randomCryptBytes, byte[] userDetails) {
            super(id, randomCryptBytes, userDetails);
        }
    }
}