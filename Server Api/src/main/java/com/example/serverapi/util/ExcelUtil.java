package com.example.serverapi.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ExcelUtil {
    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, String value) {
        Cell cell = row.createCell(currentCell, CellType.STRING);

        cell.setCellValue(value != null ? value : "");

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, Integer value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        cell.setCellValue(value == null ? 0 : value);

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, Date value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        cell.setCellValue(value == null ? null : formatter.format(value));

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, Double value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        cell.setCellValue(value == null ? 0.0 : value);

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, BigInteger value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        cell.setCellValue(value == null ? 0.0 : value.doubleValue());

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, Object value) {

        Cell cell = row.createCell(currentCell, CellType.STRING);

        cell.setCellValue(value == null ? "" : value.toString());

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, Long value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        cell.setCellValue(value == null ? 0.0 : value);

        cell.setCellStyle(cellStyle);
    }

    public static void createCell(Row row, Integer currentCell, CellStyle cellStyle, BigDecimal value) {

        Cell cell = row.createCell(currentCell, CellType.NUMERIC);

        cell.setCellValue(value == null ? 0.0 : value.doubleValue());

        cell.setCellStyle(cellStyle);
    }

    public static void setCell(Row row, Integer currentCell, CellStyle cellStyle, String value) {

        Cell cell = row.getCell(currentCell);

        cell.setCellValue(value == null ? "" : value);

        cell.setCellStyle(cellStyle);
    }


    public static CellStyle getStyle(XSSFWorkbook wb, int state) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 14);
        font.setFontName("Times New Roman");
        font.setBold(false);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);

        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(state == 0 ?
                HorizontalAlignment.CENTER : HorizontalAlignment.LEFT);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }


    public static String getCellValue(Cell cell) {
        if (cell == null) return null;
        String value = "";
        switch (cell.getCellType()) {
            case STRING -> value = cell.getStringCellValue();
            case NUMERIC -> value = String.valueOf(cell.getNumericCellValue());
        }
        return value.isBlank() ? null : value;
    }

}
