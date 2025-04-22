package com.remitly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleSwiftCodeDTO {

    private String swiftCode;
    private String bankName;
    private String address;
    private String countryISO2;
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;

}

