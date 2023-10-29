package com.aalechnovic.userdetailservice.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Ensuring we are starting the container just once, expose container properties for consumption by spring data source
 */
public class MySQLTestContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.1.0"));
    private static final Logger logger = LoggerFactory.getLogger(MySQLTestContainerInitializer.class);

    static {
        logger.info("Starting MySQL Container database...");
        mysql.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of("container.database.url=" + mysql.getJdbcUrl(),
                              "container.database.username=" + mysql.getUsername(),
                              "container.database.password=" + mysql.getPassword())
                          .applyTo(ctx.getEnvironment());
    }
}