package com.aalechnovic.userdetailservice;

import com.aalechnovic.userdetailservice.crypto.persistence.EncryptedUserDetailsRepository;
import com.aalechnovic.userdetailservice.persistence.config.MySQLTestContainerInitializer;
import com.aalechnovic.userdetailservice.web.UserDetailsResource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = MySQLTestContainerInitializer.class)
@ActiveProfiles("test")
class UserDetailsServiceIntegrationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private EncryptedUserDetailsRepository encryptedUserDetailsRepository;

	@Autowired
	private DataSource dataSource;

	@AfterEach
	public void cleanUp(){
		encryptedUserDetailsRepository.deleteAll();
	}

	@Test
	void createNewUserDetails_CREATED() throws IOException {
		String postRequest = loadFromFile("post.json");

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> httpEntity = new HttpEntity<>(postRequest, headers);

		var resEntity = testRestTemplate.postForEntity("/api/v1/users-details", httpEntity, UserDetailsResource.class);

		assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		UserDetailsResource body = resEntity.getBody();

		assertThat(body).isNotNull();

		assertThat(body.getId()).isNotNull();
		assertThat(body.getEmail()).isEqualTo("itest@itest.itest");
		assertThat(body.getName()).isEqualTo("Anton");
		assertThat(body.getSurname()).isEqualTo("Ale");
	}

	@Test
	void findUserById_OK() throws IOException {
		String postRequest = loadFromFile("post.json");

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		HttpEntity<String> httpEntity = new HttpEntity<>(postRequest, headers);

		var resEntity = testRestTemplate.postForEntity("/api/v1/users-details", httpEntity, UserDetailsResource.class);

		assertThat(resEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		UserDetailsResource body = resEntity.getBody();

		assertThat(body).isNotNull();

		Long createdId = body.getId();

		System.out.println("AAAAAABBBB" + body.getId());


		assertThat(createdId).isNotNull();

		var resGetEntity = testRestTemplate.getForEntity("/api/v1/users-details/{id}", UserDetailsResource.class, Map.of("id", createdId));

		assertThat(resGetEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

		var resGetEntityBody = resGetEntity.getBody();

		assertThat(resGetEntityBody).isNotNull();
		assertThat(resGetEntityBody.getId()).isEqualTo(createdId);
		assertThat(resGetEntityBody.getEmail()).isEqualTo("itest@itest.itest");
		assertThat(resGetEntityBody.getName()).isEqualTo("Anton");
		assertThat(resGetEntityBody.getSurname()).isEqualTo("Ale");
	}

	@Test
	void findUserById_NOT_FOUND(){
		var resGetEntity = testRestTemplate.getForEntity("/api/v1/users-details/{id}", UserDetailsResource.class, Map.of("id", 1L));

		assertThat(resGetEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@NotNull
	private static String loadFromFile(String fileName) throws IOException {
		return StreamUtils.copyToString(new ClassPathResource("itestrss/"+fileName).getInputStream(),
										Charset.defaultCharset());
	}

}
