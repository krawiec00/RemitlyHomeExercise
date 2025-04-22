package com.remitly.components;

import com.remitly.dto.SimpleSwiftCodeDTO;
import com.remitly.dto.SwiftCodeResponseDTO;
import com.remitly.entities.SwiftCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SwiftCodeMapper {

    public SwiftCodeResponseDTO toDto(SwiftCode entity) {
        if (entity == null) return null;

        SwiftCodeResponseDTO dto = mapBasicFields(entity);

        if (entity.isHq() && entity.getBranches() != null && !entity.getBranches().isEmpty()) {
            List<SwiftCodeResponseDTO> branchDtos = entity.getBranches().stream()
                    .map(this::toDtoWithoutBranches)
                    .collect(Collectors.toList());
            dto.setBranches(branchDtos);
        }

        return dto;
    }

    public SwiftCodeResponseDTO toDtoWithoutBranches(SwiftCode entity) {
        SwiftCodeResponseDTO dto = mapBasicFields(entity);
        dto.setBranches(null);
        return dto;
    }

    private SwiftCodeResponseDTO mapBasicFields(SwiftCode entity) {
        SwiftCodeResponseDTO dto = new SwiftCodeResponseDTO();
        dto.setSwiftCode(entity.getSwiftCode());
        dto.setBankName(entity.getBankName());
        dto.setAddress(entity.getAddress());
        dto.setCountryISO2(entity.getCountryISO2());
        dto.setCountryName(entity.getCountryName());
        dto.setHeadquarter(entity.isHq());
        return dto;
    }



    public SimpleSwiftCodeDTO toSimpleDto(SwiftCode entity) {
        if (entity == null) return null;

        SimpleSwiftCodeDTO dto = new SimpleSwiftCodeDTO();
        dto.setSwiftCode(entity.getSwiftCode());
        dto.setBankName(entity.getBankName());
        dto.setAddress(entity.getAddress());
        dto.setCountryISO2(entity.getCountryISO2());
        dto.setHeadquarter(entity.isHq());
        return dto;
    }

}

