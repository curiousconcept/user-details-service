package com.aalechnovic.userdetailservice;

import com.aalechnovic.userdetailservice.crypto.EncryptingUserDetailsService;
import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.util.ObjectSerializer;
import com.aalechnovic.userdetailservice.util.Pair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ObjectSerializer objectSerializer(ObjectMapper objectMapper){
        return new ObjectSerializer(objectMapper);
    }
    @Bean
    public EncryptingUserDetailsService encryptingUserDetailsService(EncryptedUserDetailsRepository userDetailsRepository,
                                                                     ObjectSerializer objectSerializer,
                                                                     @Value("${data.encryption.password}") String password,
                                                                     @Value("${data.encryption.salt}") byte[] salt) {
        return new EncryptingUserDetailsService(userDetailsRepository, objectSerializer, Pair.of(password, salt));
    }
}
