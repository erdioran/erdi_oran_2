package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelManager {
    
    private static final String DEFAULT_EXCEL_PATH = "src/test/resources/testData.xlsx";

    public static String getCellValue(String filePath, String sheetName, int rowIndex, int columnIndex) {
        String excelPath = (filePath != null) ? filePath : DEFAULT_EXCEL_PATH;
        
        try {
            FileInputStream fis = new FileInputStream(new File(excelPath));
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet bulunamadı: " + sheetName);
            }
            
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                workbook.close();
                fis.close();
                return "";
            }
            
            Cell cell = row.getCell(columnIndex);
            String cellValue = "";
            
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        cellValue = String.valueOf((int) cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        cellValue = String.valueOf(cell.getBooleanCellValue());
                        break;
                    default:
                        cellValue = "";
                }
            }
            
            workbook.close();
            fis.close();
            return cellValue;
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası okunamadı: " + e.getMessage());
        }
    }

    public static String getCellValue(String sheetName, int rowIndex, int columnIndex) {
        return getCellValue(null, sheetName, rowIndex, columnIndex);
    }

    public static String getTestData(String dataName) {
        try {
            FileInputStream fis = new FileInputStream(new File(DEFAULT_EXCEL_PATH));
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheet("TestData");
            if (sheet == null) {
                workbook.close();
                fis.close();
                throw new RuntimeException("TestData sheet'i bulunamadı");
            }

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nameCell = row.getCell(0); // A sütunu
                    if (nameCell != null && dataName.equals(nameCell.getStringCellValue())) {
                        Cell valueCell = row.getCell(1); // B sütunu
                        String result = (valueCell != null) ? valueCell.getStringCellValue() : "";
                        workbook.close();
                        fis.close();
                        return result;
                    }
                }
            }
            
            workbook.close();
            fis.close();
            throw new RuntimeException("Test verisi bulunamadı: " + dataName);
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası okunamadı: " + e.getMessage());
        }
    }

    public static int getRowCount(String sheetName) {
        try {
            FileInputStream fis = new FileInputStream(new File(DEFAULT_EXCEL_PATH));
            Workbook workbook = new XSSFWorkbook(fis);
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                workbook.close();
                fis.close();
                return 0;
            }
            
            int rowCount = sheet.getLastRowNum() + 1;
            workbook.close();
            fis.close();
            return rowCount;
            
        } catch (IOException e) {
            throw new RuntimeException("Excel dosyası okunamadı: " + e.getMessage());
        }
    }
}
