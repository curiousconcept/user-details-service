package com.aalechnovic.userdetailservice;

import com.aalechnovic.userdetailservice.crypto.EncryptingUserDetailsService;
import com.aalechnovic.userdetailservice.crypto.Encryptor;
import com.aalechnovic.userdetailservice.crypto.EncryptorImpl;
import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.util.ObjectSerializer;
import com.aalechnovic.userdetailservice.util.ObjectSerializerImpl;
import com.aalechnovic.userdetailservice.util.Pair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Dependencies instantiation and injection of the components used by the system
 */
@Configuration
public class Config {

    @Bean
    public ObjectSerializer objectSerializer(ObjectMapper objectMapper){
        return new ObjectSerializerImpl();
    }

    @Bean
    public Encryptor encryptor(){
        return new EncryptorImpl();
    }

    @Bean
    public EncryptingUserDetailsService encryptingUserDetailsService(EncryptedUserDetailsRepository userDetailsRepository,
                                                                     ObjectSerializer objectSerializer,
                                                                     Encryptor encryptor,
                                                                     @Value("${data.encryption.password}") String password,
                                                                     @Value("${data.encryption.salt}") byte[] salt) {
        return new EncryptingUserDetailsService(userDetailsRepository, objectSerializer, encryptor, Pair.of(password, salt));
    }
}
