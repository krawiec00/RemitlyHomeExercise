package com.remitly.controllers;

import com.remitly.entities.SwiftCode;
import com.remitly.homeExercise.HomeExerciseApplicationTests;
import com.remitly.repositories.SwiftCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HomeExerciseApplicationTests.class)
@AutoConfigureMockMvc
@Testcontainers
public class SwiftCodeControllerTest {

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SwiftCodeRepository swiftCodeRepository;

    @BeforeEach
    void setUp() {
        swiftCodeRepository.deleteAll();

        SwiftCode swift = new SwiftCode();
        swift.setSwiftCode("TESTPLPWXXX");
        swift.setBankName("Testowy Bank");
        swift.setAddress("Testowa 123");
        swift.setCountryISO2("PL");
        swift.setCountryName("POLAND");
        swift.setHq(true);

        swiftCodeRepository.save(swift);
    }

    @Test
    void shouldReturnSwiftCode_whenExists() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/TESTPLPWXXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("TESTPLPWXXX"))
                .andExpect(jsonPath("$.bankName").value("Testowy Bank"))
                .andExpect(jsonPath("$.countryISO2").value("PL"))
                .andExpect(jsonPath("$.isHeadquarter").value(true));
    }

    @Test
    void shouldReturn404_whenCodeNotExists() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/NOTFOUND123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found: NOTFOUND123"));
    }

    @Test
    void shouldReturnSwiftCodesByCountry_whenExist() throws Exception {
        swiftCodeRepository.deleteAll();

        SwiftCode hq = new SwiftCode();
        hq.setSwiftCode("BANKPLPWXXX");
        hq.setBankName("Bank HQ");
        hq.setAddress("HQ Street 1");
        hq.setCountryISO2("PL");
        hq.setCountryName("POLAND");
        hq.setHq(true);
        swiftCodeRepository.save(hq);

        SwiftCode branch = new SwiftCode();
        branch.setSwiftCode("BANKPLPWBMW");
        branch.setBankName("Bank Branch");
        branch.setAddress("Branch Ave 2");
        branch.setCountryISO2("PL");
        branch.setCountryName("POLAND");
        branch.setHq(false);
        branch.setHeadquarter(hq); // przypisz do HQ
        swiftCodeRepository.save(branch);

        mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("PL"))
                .andExpect(jsonPath("$.countryName").value("POLAND"))
                .andExpect(jsonPath("$.swiftCodes").isArray())
                .andExpect(jsonPath("$.swiftCodes.length()").value(2))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("BANKPLPWXXX"))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("BANKPLPWBMW"));
    }

    @Test
    void shouldReturn404_whenNoSwiftCodesForCountry() throws Exception {
        swiftCodeRepository.deleteAll();

        mockMvc.perform(get("/v1/swift-codes/country/ZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No SWIFT codes found for country: ZZ"));
    }

    @Test
    void shouldAddSwiftCodeSuccessfully() throws Exception {
        swiftCodeRepository.deleteAll();

        String requestBody = """
    {
      "swiftCode": "NEWSWIFTXXX",
      "bankName": "Nowy Bank",
      "address": "Ulica Testowa 5",
      "countryISO2": "PL",
      "countryName": "POLAND",
      "isHeadquarter": true
    }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code added successfully"));

        // dodatkowe potwierdzenie — dane naprawdę zapisane?
        Optional<SwiftCode> saved = swiftCodeRepository.findBySwiftCode("NEWSWIFTXXX");
        assertThat(saved).isPresent();
        assertThat(saved.get().getBankName()).isEqualTo("Nowy Bank");
        assertThat(saved.get().isHq()).isTrue();
    }

    @Test
    void shouldReturnConflictWhenSwiftCodeAlreadyExists() throws Exception {
        swiftCodeRepository.deleteAll();

        SwiftCode existing = new SwiftCode();
        existing.setSwiftCode("DUPLPLPWXXX");
        existing.setBankName("Bank Duplikat");
        existing.setAddress("Ulica Duplikatowa");
        existing.setCountryISO2("PL");
        existing.setCountryName("POLAND");
        existing.setHq(true);
        swiftCodeRepository.save(existing);

        String requestBody = """
    {
      "swiftCode": "DUPLPLPWXXX",
      "bankName": "Bank Duplikat",
      "address": "Ulica Duplikatowa",
      "countryISO2": "PL",
      "countryName": "POLAND",
      "isHeadquarter": true
    }
    """;

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SWIFT code already exists: DUPLPLPWXXX"));
    }

    @Test
    void shouldDeleteSwiftCodeSuccessfully() throws Exception {
        swiftCodeRepository.deleteAll();

        SwiftCode code = new SwiftCode();
        code.setSwiftCode("DELPLPWXXX");
        code.setBankName("Bank Do Usunięcia");
        code.setAddress("Kasowana 10");
        code.setCountryISO2("PL");
        code.setCountryName("POLAND");
        code.setHq(true);
        swiftCodeRepository.save(code);

        mockMvc.perform(delete("/v1/swift-codes/DELPLPWXXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully: DELPLPWXXX"));

        assertThat(swiftCodeRepository.findBySwiftCode("DELPLPWXXX")).isEmpty();
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingSwiftCode() throws Exception {
        swiftCodeRepository.deleteAll();

        mockMvc.perform(delete("/v1/swift-codes/NONEXIST123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found: NONEXIST123"));
    }




}

