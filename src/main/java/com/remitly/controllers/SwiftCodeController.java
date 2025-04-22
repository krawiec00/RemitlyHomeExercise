package com.remitly.controllers;

import com.remitly.components.SwiftCodeMapper;
import com.remitly.dto.CountrySwiftCodesResponseDTO;
import com.remitly.dto.SimpleSwiftCodeDTO;
import com.remitly.dto.SwiftCodeRequestDTO;
import com.remitly.dto.SwiftCodeResponseDTO;
import com.remitly.entities.SwiftCode;
import com.remitly.repositories.SwiftCodeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController {

    private final SwiftCodeRepository swiftCodeRepository;
    private final SwiftCodeMapper swiftCodeMapper;

    public SwiftCodeController(SwiftCodeRepository swiftCodeRepository,
                               SwiftCodeMapper swiftCodeMapper) {
        this.swiftCodeRepository = swiftCodeRepository;
        this.swiftCodeMapper = swiftCodeMapper;
    }

    @GetMapping("/{swiftCode}")
    public ResponseEntity<?> getSwiftCode(@PathVariable String swiftCode) {
        Optional<SwiftCode> optionalSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCode);

        if (optionalSwiftCode.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "SWIFT code not found: " + swiftCode));
        }

        SwiftCode entity = optionalSwiftCode.get();
        SwiftCodeResponseDTO dto = swiftCodeMapper.toDto(entity);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<?> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        List<SwiftCode> codes = swiftCodeRepository.findAllByCountryISO2(countryISO2);

        if (codes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "No SWIFT codes found for country: " + countryISO2.toUpperCase()));
        }

        String countryName = codes.get(0).getCountryName();

        List<SimpleSwiftCodeDTO> codeDtos = codes.stream()
                .map(swiftCodeMapper::toSimpleDto)
                .collect(Collectors.toList());

        CountrySwiftCodesResponseDTO response = new CountrySwiftCodesResponseDTO();
        response.setCountryISO2(countryISO2.toUpperCase());
        response.setCountryName(countryName);
        response.setSwiftCodes(codeDtos);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addSwiftCode(@RequestBody @Valid SwiftCodeRequestDTO request) {

        if (swiftCodeRepository.findBySwiftCode(request.getSwiftCode()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "SWIFT code already exists: " + request.getSwiftCode()));
        }

        SwiftCode newEntry = new SwiftCode();
        newEntry.setSwiftCode(request.getSwiftCode());
        newEntry.setBankName(request.getBankName());
        newEntry.setAddress(request.getAddress());
        newEntry.setCountryISO2(request.getCountryISO2().toUpperCase());
        newEntry.setCountryName(request.getCountryName().toUpperCase());
        newEntry.setHq(request.isHeadquarter());

        if (!request.isHeadquarter()) {
            String prefix = request.getSwiftCode().substring(0, 8);
            swiftCodeRepository.findBySwiftCode(prefix + "XXX").ifPresent(newEntry::setHeadquarter);
        }

        swiftCodeRepository.save(newEntry);

        return ResponseEntity.ok(Map.of("message", "SWIFT code added successfully"));
    }

    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<?> deleteSwiftCode(@PathVariable String swiftCode) {
        Optional<SwiftCode> swift = swiftCodeRepository.findBySwiftCode(swiftCode);

        if (swift.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "SWIFT code not found: " + swiftCode));
        }

        swiftCodeRepository.delete(swift.get());

        return ResponseEntity.ok(Map.of("message", "SWIFT code deleted successfully: " + swiftCode));
    }


}

