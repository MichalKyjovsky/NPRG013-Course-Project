package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import org.apache.poi.ss.formula.functions.Index;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;


public class SheetBuilder{
    private Workbook workbook;
    private static final int COLUMNS_WIDTH = 4000;
    private static final int HEADER_HEIGHT = 45 ;
    private static final String SHEET_PREFIX = "Budget_";
    private static final String SEPARATOR = "_";
    private static String nameOfTheDocument;
    private static final String FILE_SUFFIX = ".xlsx";

    public SheetBuilder(){
        workbook = SheetBuilderController.getBudget_tracker();
    }

    public static void setNameOfTheDocument(String name){
        if(!name.endsWith(FILE_SUFFIX)) {
            nameOfTheDocument = name + FILE_SUFFIX;
        }
        else{
            nameOfTheDocument = name;
        }
    }

    public static String getNameOfTheDocument() {
        return nameOfTheDocument;
    }

    public void setCellValue(String value){
            int sheetCount = workbook.getNumberOfSheets();
    }

    public void createNewSheet(String initialMonth){
        LocalDateTime ldt = LocalDateTime.now();
        String sheetName = SHEET_PREFIX + new DateFormatSymbols().getMonths()[Integer.parseInt(initialMonth) - 1].toUpperCase() + SEPARATOR + ldt.getYear();
        Sheet newSheet = workbook.createSheet(sheetName);

        try(FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())) {
            WorkbookBuilder workbookBuilder = new WorkbookBuilder();
            workbookBuilder.createInitialSheet(newSheet,Integer.parseInt(initialMonth),workbook);
            workbook.write(fio);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
}
    private void remapTotalColumn(Sheet sheet, int position){
        int sheetHeight = 0;
        try {
            for (int i = 0; i < 35; i++) {
                if (!sheet.getRow(i).getCell(0).getStringCellValue().isEmpty()) {
                    sheetHeight++;
                }
            }
        }catch (Exception e){};

        workbook = SheetBuilderController.getBudget_tracker();
        CellStyle cellStyle = workbook.createCellStyle();

        for (int i = 0; i < sheetHeight; i++){
            Cell oldCell = sheet.getRow(i).getCell(position - 1);
            Cell newCell = sheet.getRow(i).createCell(position);
            newCell.setCellValue(oldCell.getStringCellValue());
        }
        saveProgress();
    }

    private void saveProgress(){
        try(FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())){
            workbook = SheetBuilderController.getBudget_tracker();
            workbook.write(fio);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void setTotalHeader(CellStyle cellStyle){
        cellStyle.setFillForegroundColor(IndexedColors.DARK_RED.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

    }

    public void createNewColumn(String columnName){
        System.out.println(columnName);
//        Sheet actualSheet = SheetBuilderController.getActualSheet();
//        int sheetWidth = actualSheet.getRow(0).getLastCellNum();
//        remapTotalColumn(actualSheet,sheetWidth);
    }
}
