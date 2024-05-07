package com.example.calculation;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelBook {
    public static XSSFWorkbook openWorkbook(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return new XSSFWorkbook(fis);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
