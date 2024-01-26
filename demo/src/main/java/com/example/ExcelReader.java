package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

  public List<Customer> readExcelFile(String filePath) {
    List<Customer> dataList = new ArrayList<>();

    try (FileInputStream fis = new FileInputStream(new File(filePath));
        XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

      Sheet sheet = workbook.getSheetAt(0);

      // CONCAT ÖVNING - Skapa en ny kolumn för Full_Name
      Row headerRow = sheet.getRow(0);
      int fullNameColumnIndex = headerRow.getLastCellNum(); // Index för den nya kolumnen
      Cell newHeaderCell = headerRow.createCell(fullNameColumnIndex);
      newHeaderCell.setCellValue("Full_Name");

      for (Row row : sheet) {
        if (row.getRowNum() == 0) { // Skippar första raden (rubrikerna)
          continue;
        }

        // Läs data från varje cell i raden och skapa ett Customer-objekt
        Integer customerID = safeGetIntCellValue(row, 0);
        String firstName = safeGetStringCellValue(row, 1);
        String lastName = safeGetStringCellValue(row, 2);
        String phoneNumber = safeGetStringCellValue(row, 3);
        String address = safeGetStringCellValue(row, 4);
        String payingCustomer = safeGetStringCellValue(row, 5);
        String doNotContact = safeGetStringCellValue(row, 6);

        // CONCAT ÖVNING - Gå igenom varje rad och kombinera First_Name och Last_Name
        String fullName = firstName + " " + lastName;
        Cell newCell = row.createCell(fullNameColumnIndex);
        newCell.setCellValue(fullName);

        // CONCAT ÖVNING - Vi har uppdaterat Customer för att inkludera "fullName"
        Customer customer = new Customer(customerID, firstName, lastName,
            phoneNumber, address, payingCustomer,
            doNotContact, fullName);
        dataList.add(customer);

        // CONCAT ÖVNING - Spara den nya filen
        try (FileOutputStream outputStream = new FileOutputStream("Customer Call List - Cleaned2.xlsx")) {
          workbook.write(outputStream);
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return dataList;
  }

  private Integer safeGetIntCellValue(Row row, int cellIndex) {
    Cell cell = row.getCell(cellIndex);
    return (cell != null) ? (int) cell.getNumericCellValue() : null;
  }

  private String safeGetStringCellValue(Row row, int cellIndex) {
    Cell cell = row.getCell(cellIndex);
    return (cell != null) ? cell.getStringCellValue() : "";
  }
}