package com.remitly.homeExercise;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ComponentScan(basePackages={"com.remitly"})
public class HomeExerciseApplicationTests {


	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("testdb")
			.withUsername("swift_user")
			.withPassword("swift_pass");

	@DynamicPropertySource
	static void overrideDataSource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url",  postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Test
	void contextLoads() {
	}

}
