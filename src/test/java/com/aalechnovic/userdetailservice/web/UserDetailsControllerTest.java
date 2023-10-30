package com.aalechnovic.userdetailservice.web;

import com.aalechnovic.userdetailservice.crypto.EncryptingUserDetailsService;
import com.aalechnovic.userdetailservice.domain.UserDetails;
import com.aalechnovic.userdetailservice.util.Pair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserDetailsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EncryptingUserDetailsService encryptingUserDetailsService;

    @Test
    void testPost() throws Exception {
        UserDetailsResource userDetailsResource = new UserDetailsResource(null, "test@test.test", "testname", "testsurname");

        UserDetails userDetailsPairFrom = UserDetailsController.getUserDetailsPairFrom(userDetailsResource);


        setupSvcToReturnWhatWasPassed(userDetailsPairFrom);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users-details").content(asJsonString(userDetailsResource))
                                              .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
               .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.test"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testname"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("testsurname"));
    }


    @Test
    void testGet_resultOk() throws Exception {
        UserDetails userDetails = new UserDetails("test@test.test", "testname", "testsurname");
        Pair<Long, UserDetails> idUserDetailsPair = Pair.of(1L, userDetails);

        doReturn(Optional.of(idUserDetailsPair)).when(encryptingUserDetailsService).findById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users-details/{id}", 1L).accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
               .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@test.test"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testname"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("testsurname"));
    }

    @Test
    void testGet_notFound() throws Exception {
        doReturn(Optional.empty()).when(encryptingUserDetailsService).findById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users-details/{id}", 1L).accept(MediaType.APPLICATION_JSON))
               .andDo(print())
               .andExpect(status().isNotFound());
    }

    private void setupSvcToReturnWhatWasPassed(UserDetails expectedUserDetails) {
        doAnswer(((Answer<Pair<Long,UserDetails>>) invocation -> {
            var castInvocation = (UserDetails)  invocation.getArgument(0);

            return Pair.of(new Random().nextLong(), castInvocation);
        })).when(encryptingUserDetailsService).save(expectedUserDetails);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}