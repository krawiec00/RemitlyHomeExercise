package com.remitly.services;

import com.remitly.entities.SwiftCode;
import com.remitly.excepiton.InvalidExcelFileException;
import com.remitly.repositories.SwiftCodeRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SwiftExcelService {

    private final SwiftCodeRepository swiftCodeRepository;

    public SwiftExcelService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    public void parseAndSave(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            XSSFSheet sheet = workbook.getSheetAt(0);

            Map<String, SwiftCode> hqMap = new HashMap<>();
            List<SwiftCode> allSwiftCodes = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip headers
                if (row.getPhysicalNumberOfCells() < 7) {
                    throw new InvalidExcelFileException("Invalid Excel row format at row " + row.getRowNum());
                }
                String swiftCode = row.getCell(1).getStringCellValue();
                boolean isHeadquarter = row.getCell(1).getStringCellValue().endsWith("XXX");

                SwiftCode entity = new SwiftCode();
                entity.setSwiftCode(row.getCell(1).getStringCellValue());
                entity.setBankName(row.getCell(3).getStringCellValue().toUpperCase());
                entity.setAddress(row.getCell(4).getStringCellValue().toUpperCase());
                entity.setCountryISO2(row.getCell(0).getStringCellValue());
                entity.setCountryName(row.getCell(6).getStringCellValue().toUpperCase());
                entity.setHq(isHeadquarter);

                if (isHeadquarter) {
                    if (!swiftCodeRepository.existsBySwiftCode(swiftCode)) {
                        SwiftCode savedHQ = swiftCodeRepository.save(entity);
                        hqMap.put(swiftCode.substring(0, 8), savedHQ);
                    } else {
                        // Pominięcie HQ
                        hqMap.put(swiftCode.substring(0, 8), swiftCodeRepository.findBySwiftCode(swiftCode).orElse(null));
                    }
                } else {
                    if (!swiftCodeRepository.existsBySwiftCode(swiftCode)) {
                        allSwiftCodes.add(entity);
                    }
                }
            }

            for (SwiftCode branch : allSwiftCodes) {
                String prefix = branch.getSwiftCode().substring(0, 8);
                SwiftCode hq = hqMap.get(prefix);
                if (hq != null) {
                    branch.setHeadquarter(hq);
                }

                if (!swiftCodeRepository.existsBySwiftCode(branch.getSwiftCode())) {
                    swiftCodeRepository.save(branch);
                }
            }

        } catch (NullPointerException | IllegalStateException | IndexOutOfBoundsException | IOException e) {
            throw new InvalidExcelFileException("Invalid Excel format at" );
        }
    }
    public List<SwiftCode> getSwiftCodes(){
        return swiftCodeRepository.findAll();
    }
}
