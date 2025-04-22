package com.remitly.components;

import com.remitly.dto.SwiftCodeResponseDTO;
import com.remitly.entities.SwiftCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SwiftCodeMapperTest {

    private final SwiftCodeMapper mapper = new SwiftCodeMapper();

    @Test
    void shouldMapEntityToDto_withBranches() {
        SwiftCode hq = new SwiftCode();
        hq.setSwiftCode("HQCODEXXX");
        hq.setBankName("Bank HQ");
        hq.setAddress("Address 1");
        hq.setCountryISO2("PL");
        hq.setCountryName("POLAND");
        hq.setHq(true);

        SwiftCode branch = new SwiftCode();
        branch.setSwiftCode("HQCODE1234");
        branch.setBankName("Bank HQ");
        branch.setAddress("Address 2");
        branch.setCountryISO2("PL");
        branch.setCountryName("POLAND");
        branch.setHq(false);
        branch.setHeadquarter(hq);

        hq.setBranches(List.of(branch));

        SwiftCodeResponseDTO dto = mapper.toDto(hq);

        assertThat(dto.getSwiftCode()).isEqualTo("HQCODEXXX");
        assertThat(dto.isHeadquarter()).isTrue();
        assertThat(dto.getBranches()).hasSize(1);
        assertThat(dto.getBranches().get(0).getSwiftCode()).isEqualTo("HQCODE1234");
    }

    @Test
    void shouldMapEntityToSimpleDtoWithoutBranches() {
        SwiftCode code = new SwiftCode();
        code.setSwiftCode("BRANCH123");
        code.setBankName("Bank");
        code.setAddress("Address");
        code.setCountryISO2("PL");
        code.setCountryName("POLAND");
        code.setHq(false);

        SwiftCodeResponseDTO dto = mapper.toDtoWithoutBranches(code);

        assertThat(dto.getSwiftCode()).isEqualTo("BRANCH123");
        assertThat(dto.isHeadquarter()).isFalse();
        assertThat(dto.getBranches()).isNull();
    }
}

