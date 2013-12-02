/*
 * ReportToXLS.java
 *
 * 
 */
package com.realtor.rets.compliance;

import java.io.*;

import java.util.*;

//import org.apache.poi.hssf.dev.*;
//import org.apache.poi.hssf.eventmodel.*;
//import org.apache.poi.hssf.model.*;
//import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
//import org.apache.poi.poifs.filesystem.*;

/**
 * Writes TestReport object to an Microshaft Excel file (*.xls)
 * 
 * @author pobrien
 */
public class ReportToXLS {
  /** Creates a new instance of ReportToXLS */
  HSSFWorkbook wb = null;


  /** cell style for headers */
  HSSFCellStyle headerStyle = null;

  /** cell style for labels */
  HSSFCellStyle labelStyle = null;

  /**
   * Creates a new ReportToXLS object.
   */
  public ReportToXLS() {
  }

  /**
   * Adds a report to the workbook, if the workbook object is null, creates a new
   * workbood.  For each TestReport a new sheet is added to the workbook.  Call this 
   * method for every TestReport object to be represented 
   *
   * @param report report to add to the workbook
   */
  public void addReport(TestReport report) {
    try {
      if (wb == null) {
        wb = new HSSFWorkbook();

        labelStyle = wb.createCellStyle();
      }

      HSSFCellStyle style = wb.createCellStyle();
      style.setFillForegroundColor(HSSFColor.LIME.index);
      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      style.setBorderBottom((short) 1);
      style.setBorderTop((short) 1);
      style.setBorderLeft((short) 1);
      style.setBorderRight((short) 1);

      HSSFSheet sheet = wb.createSheet(report.getName());


      // Create a row and put some cells in it. Rows are 0 based.
      addLabel("Title", sheet, (short) 0, (short) 0);
      addLabel(report.getName(), sheet, (short) 0, (short) 1);

      addLabel("Description", sheet, (short) 1, (short) 0);
      addLabel(report.getDescription(), sheet, (short) 1, (short) 1);

      addLabel("Config File", sheet, (short) 2, (short) 0);
      addLabel(report.getTestConfigFile(), sheet, (short) 2, (short) 1);

      this.setStyle(style, sheet, (short) 0, (short) 0);
      this.setStyle(style, sheet, (short) 1, (short) 0);
      this.setStyle(style, sheet, (short) 2, (short) 0);

      Collection col = report.getTestResults();
      addReportHeaders(col, sheet, (short) 4, (short) 0);
      addReportResults(col, sheet, (short) 5, (short) 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a label cell, formated loke other labels
   *
   * @param label text of the label
   * @param sheet workbook sheet
   * @param x column
   * @param y row
   */
  private void addLabel(String label, HSSFSheet sheet, short x, short y) {
    HSSFRow row = sheet.getRow((short) x);

    if (row == null) {
      row = sheet.createRow((short) x);
    }

    HSSFCell cell = row.createCell((short) y);
    cell.setCellValue(label);
    cell.setCellStyle(labelStyle);
  }

  /**
   * Sets the style
   *
   * @param style style element, what the cell looks like!
   * @param sheet workbook sheet
   * @param x column
   * @param y row
   */
  private void setStyle(HSSFCellStyle style, HSSFSheet sheet, short x, short y) {
    HSSFRow row = sheet.getRow((short) x);

    if (row == null) {
      row = sheet.createRow((short) x);
    }

    HSSFCell cell = row.getCell((short) y);

    if (cell == null) {
      cell = row.createCell((short) y);
    }

    cell.setCellStyle(style);
  }

  /**
   * Add report results to the sheet
   *
   * @param results collection of testResult objects
   * @param sheet workbook sheet
   * @param x start column
   * @param y start row
   */
  private void addReportResults(Collection results, HSSFSheet sheet, short x, 
                                short y) {
    Iterator itr = results.iterator();

    while (itr.hasNext()) {
      TestResult tResult = (TestResult) itr.next();
      short i = y;
      HSSFRow row = sheet.createRow(x++);

      HSSFCell cell = row.createCell(i++);
      cell.getCellStyle().setWrapText(true);
      cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
      cell.getCellStyle().setFillPattern(HSSFCellStyle.NO_FILL);

      cell.setCellValue(tResult.getName());

      HSSFCell cell2 = row.createCell(i++);
      cell2.setCellValue(tResult.getStatus());

      HSSFCell cell1 = row.createCell(i++);
      cell1.setCellValue(tResult.getDescription());

      HSSFCell cell4 = row.createCell(i++);
      cell4.setCellValue(tResult.getNotes());

      HSSFCell cell3 = row.createCell(i++);
      cell3.setCellValue(tResult.getEvaluatorClass());
      
      HSSFCell cell5 = row.createCell(i++);
      cell5.setCellValue(tResult.getSpecificationReference());      
    }
  }

  /**
   * Put headers for testResult reports
   *
   * @param results test results
   * @param sheet workbook sheets
   * @param x start column
   * @param y start row
   */
  private void addReportHeaders(Collection results, HSSFSheet sheet, short x, 
                                short y) {
    Iterator itr = results.iterator();

    while (itr.hasNext()) {
      TestResult tResult = (TestResult) itr.next();
      short i = y;
      HSSFRow row = sheet.createRow(x++);

      HSSFCellStyle style = wb.createCellStyle();


      //style.setFillBackgroundColor(HSSFColor.YELLOW.index);
      //style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
      style.setFillForegroundColor(HSSFColor.LIME.index);
      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

      HSSFCell cell = row.createCell(i++);
      cell.setCellStyle(style);


      //cell.getCellStyle().setWrapText(true);
      //cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
      sheet.setColumnWidth((short) 0, (short) ((30 * 8) / ((double) 1 / 20)));
      sheet.setColumnWidth((short) 1, (short) ((15 * 8) / ((double) 1 / 20)));
      sheet.setColumnWidth((short) 2, (short) ((60 * 8) / ((double) 1 / 20)));
      sheet.setColumnWidth((short) 3, (short) ((75 * 8) / ((double) 1 / 20)));
      sheet.setColumnWidth((short) 4, (short) ((60 * 8) / ((double) 1 / 20)));
      sheet.setColumnWidth((short) 5, (short) ((60 * 8) / ((double) 1 / 20)));

      cell.setCellValue("Test Name");

      HSSFCell cell2 = row.createCell(i++);
      cell2.setCellValue("Status");
      cell2.setCellStyle(style);

      HSSFCell cell1 = row.createCell(i++);
      cell1.setCellStyle(style);
      cell1.setCellValue("Test Description");

      HSSFCell cell4 = row.createCell(i++);
      cell4.setCellValue("Notes");
      cell4.setCellStyle(style);

      HSSFCell cell3 = row.createCell(i++);
      cell3.setCellValue("Evaluator Java Class");
      cell3.setCellStyle(style);
      
      HSSFCell cell5 = row.createCell(i++);
      cell5.setCellValue("Specification Reference");
      cell5.setCellStyle(style);
      
    }
  }

  /**
   * Write a report to a file
   *
   * @param filename filename
   */
  public void writeReportXSL(String filename) {
    try {
      FileOutputStream fileOut = new FileOutputStream(filename);
      wb.write(fileOut);
      fileOut.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}