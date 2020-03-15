package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;

public class SheetBuilder{
    private Workbook workbook;
    private static final int COLUMNS_WIDTH = 4000;
    private static final int HEADER_HEIGHT = 40 ;
    private static final String SHEET_PREFIX = "Budget_";
    private static final String SEPARATOR = "_";
    private static String nameOfTheDocument;
    private static final String FILE_SUFFIX = ".xlsx";

    public static int getSheetHeight() {
        return sheetHeight;
    }

    private static int sheetHeight;
    private static int sheetWidth;

    public SheetBuilder(){
        workbook = SheetBuilderController.getBudget_tracker();
    }


    /**
     * Method will validate oath to the document
     * @param name path to the document
     */
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


    /**
     * Method updates values of cells depending on user input from GUI.
     * @param value user's input
     * @param actualSheet
     * @param columnIndex
     * @param rowIndex
     */
    public void setCellValue(String value,Sheet actualSheet, int columnIndex, int rowIndex){
            Double var = Double.parseDouble(value);
            actualSheet.getRow(rowIndex).getCell(columnIndex).setCellValue(String.format("%.2f",var));
            recalculateTotal(actualSheet);
            saveProgress();
    }


    /**
     * When user in GUI choose to add new sheet, method will initiate new sheet
     * with predefined pattern and naming conventions
     * @param initialMonth
     */
    public void createNewSheet(String initialMonth) {
        LocalDateTime ldt = LocalDateTime.now();
        String sheetName = SHEET_PREFIX + new DateFormatSymbols().getMonths()[Integer.parseInt(initialMonth) - 1].toUpperCase() + SEPARATOR + ldt.getYear();
        Sheet newSheet = workbook.createSheet(sheetName);

        try (FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())) {
            WorkbookBuilder workbookBuilder = new WorkbookBuilder();
            workbookBuilder.createInitialSheet(newSheet, Integer.parseInt(initialMonth), workbook);
            workbook.write(fio);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    /**
     * Method calculates number of records in particular sheet
     * @param sheet actual sheet
     * @return number of records in the sheet
     */
    public int calcSheetHeight(Sheet sheet){
            sheetHeight = 0;
            try {
                for (int i = 0; i < 35; i++) {
                    if (!sheet.getRow(i).getCell(0).getStringCellValue().isEmpty()) {
                        sheetHeight++;
                    }
                }
            }catch (Exception e){
            }
        return sheetHeight;
        }


    /**
     * Method secures correct remapping of TOTAL column
     * whenever new column is added.
     * @param sheet actual sheet
     * @param position position of new added column
     * @param columnName name of the new added column
     */
    private void remapTotalColumn(Sheet sheet, int position, String columnName){
        workbook = SheetBuilderController.getBudget_tracker();
        CellStyle totalHeaderStyle = workbook.createCellStyle();
        CellStyle defaultHeaderStyle = workbook.createCellStyle();
        CellStyle defaultStyle = workbook.createCellStyle();
        setTotalHeader(totalHeaderStyle);
        setDefaultHeader(defaultHeaderStyle);
        setDefaultDesign(defaultStyle);
        sheet.setColumnWidth(position,COLUMNS_WIDTH);

        for (int i = 0; i < sheetHeight; i++){
            Cell oldCell = sheet.getRow(i).getCell(position - 1);

            Cell newCell = sheet.getRow(i).createCell(position);
            newCell.setCellValue(oldCell.getStringCellValue());

            if(i == 0) {
                newCell.setCellStyle(totalHeaderStyle);
                oldCell.setCellStyle(defaultHeaderStyle);
                oldCell.setCellValue(columnName);
            }
            else{
                newCell.setCellStyle(defaultStyle);
                oldCell.setCellValue(String.format("%.2f", (double) 0));
            }
        }
    }


    /**
     * Method will save permanently all changes.
     */
    private void saveProgress(){
        try(FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())){
            workbook = SheetBuilderController.getBudget_tracker();
            workbook.write(fio);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


    /**
     * Method sets predefined cell design for cells which will contain some value.
     * @param cellStyle
     */
    private void setDefaultDesign(CellStyle cellStyle){
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Calibri");
        font.setFontHeight(11);
        cellStyle.setFont(font);
    }

    /**
    * Method sets predefined cell design header section.
    * @param cellStyle
    */
    private void setCommonHeaderDesign(CellStyle cellStyle){
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setBold(true);
        font.setFontName("Calibri");
        font.setColor(IndexedColors.WHITE.index);
        font.setFontHeight(11);
        cellStyle.setFont(font);
    }


    /**
     * Method sets design for all headers nor the TOTAL column.
     * @param cellStyle
     */
    private void setDefaultHeader(CellStyle cellStyle){
        cellStyle.setFillForegroundColor(IndexedColors.BLACK.index);
        setCommonHeaderDesign(cellStyle);
    }

    /**
     * Method sets design for TOTAL column header.
     * @param cellStyle
     */
    private void setTotalHeader(CellStyle cellStyle){
        cellStyle.setFillForegroundColor(IndexedColors.DARK_RED.index);
        setCommonHeaderDesign(cellStyle);
    }


    /**
     * Initiate method for creating new column on user requirement
     * in accordance to predefined design pattern.
     * @param columnName
     */
    public void createNewColumn(String columnName){
        Sheet actualSheet = SheetBuilderController.getActualSheet();
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        calcSheetHeight(actualSheet);
        remapTotalColumn(actualSheet,sheetWidth,columnName);
        recalculateTotal(actualSheet);
        saveProgress();
    }


    /**
     * Method secures proper deletion and remapping of given column.
     * @param columnName
     */
    public void deleteColumn(String columnName){
        Sheet actualSheet = SheetBuilderController.getActualSheet();
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        int indexOfDeletedColumn = 0;

        for(int i = 0; i < sheetWidth; i++){
            if(columnName.equals(actualSheet.getRow(0).getCell(i).getStringCellValue())){
                indexOfDeletedColumn = actualSheet.getRow(0).getCell(i).getColumnIndex();
            }
        }

        eraseColumnByIndex(indexOfDeletedColumn,actualSheet);
        remapColumnAfterErasing(indexOfDeletedColumn,actualSheet);
        saveProgress();
    }

    /**
     * Method on given index erase given column data.
     * @param columnIndex
     * @param actualSheet
     */
    private void eraseColumnByIndex(int columnIndex,Sheet actualSheet){
        for(int i = 0; i < sheetHeight; i++){
            actualSheet.getRow(i).removeCell(actualSheet.getRow(i).getCell(columnIndex));
        }
    }


    /**
     * Method secures proper remapping after column deletion.
     * @param columnIndex
     * @param actualSheet
     */
    private void remapColumnAfterErasing(int columnIndex, Sheet actualSheet){
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        workbook = SheetBuilderController.getBudget_tracker();
        calcSheetHeight(actualSheet);
        CellStyle cellStyle = workbook.createCellStyle();
        CellStyle headerTotal = workbook.createCellStyle();
        setDefaultDesign(cellStyle);
        for(int i = columnIndex + 1; i < sheetWidth; i++ ){
            for (int j = 0; j < sheetHeight; j++){

                Cell newCell = actualSheet.getRow(j).createCell(i-1);
                newCell.setCellValue(actualSheet.getRow(j).getCell(i).getStringCellValue());


                if(i == sheetWidth - 1 && j == 0){
                    setTotalHeader(headerTotal);
                    newCell.setCellStyle(headerTotal);
                }
                if(i == sheetWidth - 1 && j > 0){
                    newCell.setCellStyle(cellStyle);
                }
            }
            eraseColumnByIndex(i,actualSheet);
        }
        recalculateTotal(actualSheet);

    }


    /**
     * Method recalculates values int TOTAL column whenever
     * action in sheet is performed.
     * @param actualSheet
     */
    private void recalculateTotal(Sheet actualSheet){
        double sum = 0;

        for(int i = 1; i < sheetHeight; i++ ){
            for (int j = 1; j < actualSheet.getRow(0).getLastCellNum() - 1; j++ ){
                sum += Double.parseDouble(actualSheet.getRow(i).getCell(j).getStringCellValue());
            }
            actualSheet.getRow(i).getCell(actualSheet.getRow(0).getLastCellNum() -1).setCellValue(String.format("%.2f CZK",sum));
            sum = 0;
        }
    }
}
