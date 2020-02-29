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
    private static int initialMonth;
    private String SHEET_PREFIX = "Budget_";

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

        File currDir = new File(System.getProperty("user.home"));
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + SheetBuilder.getNameOfTheDocument();
        System.out.println(fileLocation);


        try(FileOutputStream outputStream = new FileOutputStream(fileLocation)){
            workbook = new XSSFWorkbook(fileLocation);

            //Implementation of rows on request for initial month
            LocalDateTime ldt = LocalDateTime.now();

            Sheet sheet_01 = workbook.createSheet(SHEET_PREFIX + new DateFormatSymbols().getMonths()[initialMonth - 1].toUpperCase() + ldt.getYear());
            System.out.println(sheet_01);
            sheet_01.setColumnWidth(0,120);
            sheet_01.setColumnWidth(1,120);
            Row header = sheet_01.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.BLACK.index);
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFFont font = ((XSSFWorkbook) workbook).createFont();
            font.setFontName("Calibri");
            font.setColor(IndexedColors.WHITE.index);
            font.setBold(true);
            font.setFontHeight(11);

            headerStyle.setFont(font);

            Cell headerCell_0  = header.createCell(0);
            headerCell_0.setCellValue("DATE");
            headerCell_0.setCellStyle(headerStyle);

            Cell headerCell_1  = header.createCell(0);
            headerCell_1.setCellValue("TOTAL");
            headerCell_1.setCellStyle(headerStyle);

            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            Row row = sheet_01.createRow(2);
            Cell cell = row.createCell(0);
            cell.setCellValue(ldt.toString());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(20);
            cell.setCellStyle(style);

            workbook.write(outputStream);
            workbook.close();
        }catch (IOException ioe){
            throw new FileFormatException();
        }
         return workbook;
    }

}
