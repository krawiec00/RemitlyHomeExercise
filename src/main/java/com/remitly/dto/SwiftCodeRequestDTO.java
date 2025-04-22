package com.remitly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwiftCodeRequestDTO {

    @NotBlank
    @JsonProperty("swiftCode")
    private String swiftCode;

    @NotBlank
    @JsonProperty("bankName")
    private String bankName;

    @NotBlank
    @JsonProperty("address")
    private String address;

    @NotBlank
    @JsonProperty("countryISO2")
    private String countryISO2;

    @NotBlank
    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;

}

