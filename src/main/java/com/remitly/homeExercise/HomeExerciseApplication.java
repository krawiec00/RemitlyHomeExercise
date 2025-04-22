package com.remitly.homeExercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages={"com.remitly"})
@EnableJpaRepositories(basePackages="com.remitly.repositories")
@EntityScan(basePackages="com.remitly.entities")
public class HomeExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeExerciseApplication.class, args);
	}

}
