package com.remitly.repositories;

import com.remitly.entities.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {

    Optional<SwiftCode> findBySwiftCode(String swiftCode);

    List<SwiftCode> findAllByCountryISO2(String countryISO2);

    boolean existsBySwiftCode(String swiftCode);
}

