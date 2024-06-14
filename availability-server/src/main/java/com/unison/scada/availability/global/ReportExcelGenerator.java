package com.unison.scada.availability.global;

import lombok.Getter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ReportExcelGenerator implements ExcelGenerator {

    @Getter
    private final Workbook workbook;
    private final Sheet sheet;
    private int rowCount;
    private final XSSFFont headerXSSFFont;
    private final XSSFFont bodyXSSFFont;

    private final XSSFCellStyle headerXssfCellStyle;
    private final XSSFCellStyle bodyXssfCellStyle;

    public ReportExcelGenerator(){
        this("default");
    }

    public ReportExcelGenerator(String sheetName){
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName); // 엑셀 sheet 이름
        sheet.setDefaultColumnWidth(20); // 디폴트 너비 설정
        rowCount = 0;

        headerXSSFFont = (XSSFFont) workbook.createFont();
        bodyXSSFFont = (XSSFFont) workbook.createFont();

        headerXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();
        bodyXssfCellStyle = (XSSFCellStyle) workbook.createCellStyle();

        /*
        * Set Header Border
        * */
        headerXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        headerXssfCellStyle.setBorderRight(BorderStyle.THIN);
        headerXssfCellStyle.setBorderTop(BorderStyle.THIN);
        headerXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        headerXssfCellStyle.setAlignment(HorizontalAlignment.CENTER);

        headerXSSFFont.setFamily(FontFamily.MODERN);
        headerXSSFFont.setBold(true);
        headerXssfCellStyle.setFont(headerXSSFFont);

        /*
        * Set Body Border
        * */
        bodyXssfCellStyle.setBorderLeft(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderRight(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderTop(BorderStyle.THIN);
        bodyXssfCellStyle.setBorderBottom(BorderStyle.THIN);

        bodyXssfCellStyle.setAlignment(HorizontalAlignment.CENTER);

    }
    public void setHeaderFontColor(XSSFColor xssfColor){
        headerXSSFFont.setColor(xssfColor);

        headerXssfCellStyle.setFont(headerXSSFFont);
    }

    public void setHeaderCellBackgroundColor(XSSFColor xssFColor){
        headerXssfCellStyle.setFillForegroundColor(xssFColor);
        headerXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void setBodyFontColor(XSSFColor xssfColor){
        bodyXSSFFont.setColor(xssfColor);

        bodyXssfCellStyle.setFont(bodyXSSFFont);
    }
    public void setBodyCellBackgroundColor(XSSFColor xssFColor){
        bodyXssfCellStyle.setFillForegroundColor(xssFColor);
        bodyXssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    @Override
    public void setHeaderNames(List<String> names) {
        Row headerRow;
        Cell headerCell;

        headerRow = sheet.createRow(rowCount++);

        for(int i=0; i<names.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(names.get(i)); // 데이터 추가
            headerCell.setCellStyle(headerXssfCellStyle); // 스타일 추가
        }
    }

    @Override
    public void setBodyDatas(List<List<String>> bodyDatass) {
        Row bodyRow;
        Cell bodyCell;

        for(List<String> bodyDatas : bodyDatass) {
            bodyRow = sheet.createRow(rowCount++);

            for(int i=0; i<bodyDatas.size(); i++) {
                bodyCell = bodyRow.createCell(i);
                bodyCell.setCellValue(bodyDatas.get(i)); // 데이터 추가
                bodyCell.setCellStyle(bodyXssfCellStyle); // 스타일 추가
            }
        }
    }
}
