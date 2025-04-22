package com.remitly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "address",
        "bankName",
        "countryISO2",
        "countryName",
        "isHeadquarter",
        "swiftCode",
        "branches"
})
public class SwiftCodeResponseDTO {
    private String swiftCode;
    private String bankName;
    private String address;
    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeResponseDTO> branches;
}
