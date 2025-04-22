package com.remitly.dto;

import com.remitly.homeExercise.HomeExerciseApplicationTests;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class SwiftCodeRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldFailValidation_whenRequiredFieldsAreBlank() {
        SwiftCodeRequestDTO dto = SwiftCodeRequestDTO.builder()
                .swiftCode("")
                .bankName("")
                .address("")
                .countryISO2("")
                .countryName("")
                .isHeadquarter(true)
                .build();

        Set<ConstraintViolation<SwiftCodeRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(5);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("swiftCode"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("bankName"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("address"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("countryISO2"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("countryName"));
    }

    @Test
    void shouldPassValidation_whenAllRequiredFieldsAreValid() {
        SwiftCodeRequestDTO dto = SwiftCodeRequestDTO.builder()
                .swiftCode("BANKPLPWXXX")
                .bankName("Test Bank")
                .address("Testowa 1")
                .countryISO2("PL")
                .countryName("POLAND")
                .isHeadquarter(true)
                .build();

        Set<ConstraintViolation<SwiftCodeRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }
}
