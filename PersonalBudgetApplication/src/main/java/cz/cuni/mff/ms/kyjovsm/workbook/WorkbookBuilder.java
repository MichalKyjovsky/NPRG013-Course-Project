package cz.cuni.mff.ms.kyjovsm.workbook;


import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class WorkbookBuilder {

    private Workbook workbook;
    private LocalDateTime ldt;
    private static  String  path;
    private static int initialMonth;
    private static final String SHEET_PREFIX = "Budget_";
    private static final String SEPARATOR = "_";
    private static final String USER_HOME_DIR = "user.home";
    private static final int COLUMNS_WIDTH = 4000;
    private static final int HEADER_HEIGHT = 60 ;
    private static final String TOTAL_HEADING = "TOTAL";
    private static final String DATE_HEADING = "DATE";
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String ACCOUNT_FORMAT = "#,##0.00";

    public static String getPath() {
        return path;
    }

    public static void setInitialMonth(int initialMonth) {
        WorkbookBuilder.initialMonth = initialMonth;
    }

    public static int getInitialMonth() {
        return initialMonth;
    }

    public Workbook createFromExistingFile() throws FileFormatException{
        workbook = null;

        if(!SheetBuilder.getNameOfTheDocument().strip().isEmpty())
        try(FileInputStream fis = new FileInputStream(new File(SheetBuilder.getNameOfTheDocument()))){
            workbook = new XSSFWorkbook(fis);
        }catch (IOException ioe){
            throw new FileFormatException();
        }
        return workbook;
    }

    public Workbook createInitialWorkbook() throws FileFormatException{
        workbook = null;

        File currDir = new File(System.getProperty(USER_HOME_DIR));
        path = currDir.getAbsolutePath() + File.separator + SheetBuilder.getNameOfTheDocument();

        try(FileOutputStream outputStream = new FileOutputStream(path)){
            workbook = new XSSFWorkbook();

            //Implementation of rows on request for initial month
            ldt = LocalDateTime.now();

            Sheet sheet_01 = workbook.createSheet(SHEET_PREFIX + new DateFormatSymbols().getMonths()[initialMonth - 1].toUpperCase() + SEPARATOR + ldt.getYear());
            createInitialSheet(sheet_01,initialMonth, this.workbook);
            workbook.write(outputStream);
        }catch (IOException ioe){
            throw new FileFormatException();
        }
         return workbook;
    }

    void createInitialSheet(Sheet newSheet, int initialMonth,Workbook workbook){
        ldt = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.of(ldt.getYear(),initialMonth);
        newSheet.setDefaultColumnWidth(COLUMNS_WIDTH);
        Row header = newSheet.createRow(0);
        header.setHeightInPoints(HEADER_HEIGHT);
        CellStyle headerStyleBlack = workbook.createCellStyle();
        CellStyle headerStyleRed = workbook.createCellStyle();
        setBasicHeader(headerStyleBlack, headerStyleRed);


        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        headerStyleBlack.setFont(setFont(font,"Calibri"));
        headerStyleRed.setFont(setFont(font,"Calibri"));

        Cell headerCell_0 = header.createCell(0);
        headerCell_0.setCellValue(DATE_HEADING);
        headerCell_0.setCellStyle(headerStyleBlack);

        Cell headerCell_1 = header.createCell(1);
        headerCell_1.setCellValue(TOTAL_HEADING);
        headerCell_1.setCellStyle(headerStyleRed);


        DataFormat format = workbook.createDataFormat();

        CellStyle style_date = setBasicCellStyle(workbook);
        CellStyle style_total = setBasicCellStyle(workbook);
        style_date.setDataFormat(format.getFormat(DATE_FORMAT));
        style_total.setDataFormat(format.getFormat(ACCOUNT_FORMAT));
        int currentMonth = yearMonth.getMonthValue();
        String actualMonth = String.valueOf(currentMonth);

        if (currentMonth < 10) {
            actualMonth = String.format("0%d", currentMonth);
        }

        for (int i = 1; i < yearMonth.lengthOfMonth() + 1; i++) {
            Row row = newSheet.createRow(i);
            row.setHeightInPoints(15);
            Cell cell_0 = row.createCell(0);
            Cell cell_1 = row.createCell(1);
            cell_0.setCellStyle(style_date);
            cell_1.setCellStyle(style_total);

            if (i < 10) {
                cell_0.setCellValue(String.format("%d%d.%s.%d", 0, i, actualMonth, ldt.getYear()));
            } else {
                cell_0.setCellValue(String.format("%d.%s.%d", i, actualMonth, ldt.getYear()));
            }
            cell_1.setCellValue(String.format("%.2f CZK", (double) 0));
        }
    }

    private CellStyle setBasicCellStyle(Workbook workbook){
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);

        return style;
    }

    private void setBasicHeader(CellStyle headerStyleBlack, CellStyle headerStyleRed){
        headerStyleBlack.setFillForegroundColor(IndexedColors.BLACK.index);
        headerStyleRed.setFillForegroundColor(IndexedColors.DARK_RED.index);
        headerStyleBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleBlack.setAlignment(HorizontalAlignment.CENTER);
        headerStyleRed.setAlignment(HorizontalAlignment.CENTER);
    }

    private XSSFFont setFont(XSSFFont font, String fontStyle){
        font.setFontName(fontStyle);
        font.setColor(IndexedColors.WHITE.index);
        font.setBold(true);
        font.setFontHeight(11);
        return font;
    }
}
