package com.remitly.excepiton;


public class InvalidExcelFileException extends RuntimeException {
    public InvalidExcelFileException(String message) {
        super(message);
    }
}
