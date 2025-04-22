package com.remitly.controllers;

import com.remitly.entities.SwiftCode;
import com.remitly.services.SwiftExcelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftUploadController {

    private final SwiftExcelService swiftExcelService;

    public SwiftUploadController(SwiftExcelService swiftExcelService) {
        this.swiftExcelService = swiftExcelService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body("Invalid file. Please upload an Excel (.xlsx) file.");
        }
        swiftExcelService.parseAndSave(file);
        return ResponseEntity.ok("File uploaded and processed successfully.");
    }

    @GetMapping("/print")
    public ResponseEntity<List<SwiftCode>> getSwiftCodes(){
        return ResponseEntity.ok(swiftExcelService.getSwiftCodes());
    }
}
