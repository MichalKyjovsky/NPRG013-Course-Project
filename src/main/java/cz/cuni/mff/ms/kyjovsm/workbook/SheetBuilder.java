package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.calculator.CalcEntryPoint;
import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SheetBuilder {
    /**
     * Instance of the current opened Workbook.
     */
    private Workbook workbook;
    /**
     * Default column width.
     */
    private static final int COLUMNS_WIDTH = 4000;
    /**
     * Default column height.
     */
    private static final int HEADER_HEIGHT = 40;
    /**
     * Static Sheet name prefix.
     */
    private static final String SHEET_PREFIX = "Budget_";
    /**
     * Default Sheet's name separator.
     */
    private static final String SEPARATOR = "_";
    /**
     * Static name of the Workbook.
     */
    private static String nameOfTheDocument;
    /**
     * Default file suffix.
     */
    private static final String FILE_SUFFIX = ".xlsx";

    /**
     * Default font for the workbook.
     */
    private static final String CALIBRI_FONT = "Calibri";

    private static final String SAVE_ERROR = "Unsuccessful Auto Save.";

    /**
     * Constant storing value of xslx formatting style for currency.
     */
    private static final String CZECH_CURR_FORMAT = "%.2f CZK";

    /**
     * @return number of rows in the sheet.
     */
    public static int getSheetHeight() {
        return sheetHeight;
    }

    /**
     * Variables containing number of rows in the sheet.
     */
    private static int sheetHeight;
    /**
     * Number of active columns in the sheet.
     */
    private static int sheetWidth;
    /**
     * Default size of the font.
     */
    private static int fontSize = 11;

    /**
     * An instance of class logger for creating debugging log messages.
     */
    private static Logger logger =
            Logger.getLogger(WorkbookBuilder.class.getName());

    /**
     * Constructor method.
     */
    public SheetBuilder() {
        workbook = SheetBuilderController.getBudgetTracker();
    }

    /**
     * Method will validate oath to the document.
     * @param name path to the document
     */
    public static void setNameOfTheDocument(final String name) {
        if (!name.endsWith(FILE_SUFFIX)) {
            nameOfTheDocument = name + FILE_SUFFIX;
        } else {
            nameOfTheDocument = name;
        }
    }

    /**
     * @return return full path with file type suffix of the file.
     */
    public static String getNameOfTheDocument() {
        return nameOfTheDocument;
    }

    /**
     * Method updates values of cells depending on user input from GUI.
     * @param value user's input
     * @param actualSheet actual sheet name
     * @param columnIndex actual column index
     * @param rowIndex actual row index
     */
    public void setCellValue(final String value, final Sheet actualSheet, final int columnIndex, final int rowIndex) {
        CalcEntryPoint calc = new CalcEntryPoint();
        Double var = Double.parseDouble(calc.calc(value));
        actualSheet.getRow(rowIndex).getCell(columnIndex).setCellValue(String.format("%.2f", var));
        recalculateTotal(actualSheet);
        saveProgress();
    }

    /**
     * When user in GUI choose to add new sheet, method will initiate new sheet.
     * with predefined pattern and naming conventions
     * @param initialMonth initial tracking month
     */
    public void createNewSheet(final int initialMonth) {
        LocalDateTime ldt = LocalDateTime.now();
        String sheetName = SHEET_PREFIX + new DateFormatSymbols().getMonths()[initialMonth - 1].toUpperCase() + SEPARATOR + ldt.getYear();
        Sheet newSheet = workbook.createSheet(sheetName);

        try (FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())) {
            WorkbookBuilder workbookBuilder = new WorkbookBuilder();
            workbookBuilder.createInitialSheet(newSheet, initialMonth, workbook);
            workbook.write(fio);
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,SAVE_ERROR,ioe);
        }
    }

    /**
     * Method calculates number of records in particular sheet.
     * @param sheet actual sheet
     * @return number of records in the sheet
     */
    public int calcSheetHeight(final Sheet sheet) {
            sheetHeight = 0;
            int maxHeight = 32;
            try {
                for (int i = 0; i < maxHeight; i++) {
                    if (!sheet.getRow(i).getCell(0).getStringCellValue().isEmpty()) {
                        sheetHeight++;
                    }
                }
            } catch (Exception e) {
                // CAN BE IGNORED
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
    private void remapTotalColumn(final Sheet sheet, final int position, final String columnName) {
        workbook = SheetBuilderController.getBudgetTracker();
        CellStyle totalHeaderStyle = workbook.createCellStyle();
        CellStyle defaultHeaderStyle = workbook.createCellStyle();
        CellStyle defaultStyle = workbook.createCellStyle();
        setTotalHeader(totalHeaderStyle);
        setDefaultHeader(defaultHeaderStyle);
        setDefaultDesign(defaultStyle);
        sheet.setColumnWidth(position, COLUMNS_WIDTH);

        for (int i = 0; i < sheetHeight; i++) {
            Cell oldCell = sheet.getRow(i).getCell(position - 1);

            Cell newCell = sheet.getRow(i).createCell(position);
            newCell.setCellValue(oldCell.getStringCellValue());

            if (i == 0) {
                newCell.setCellStyle(totalHeaderStyle);
                oldCell.setCellStyle(defaultHeaderStyle);
                oldCell.setCellValue(columnName);
            } else {
                newCell.setCellStyle(defaultStyle);
                oldCell.setCellValue(String.format("%.2f", (double) 0));
            }
        }
    }

    /**
     * Method will save permanently all changes.
     */
    private void saveProgress() {
        try (FileOutputStream fio = new FileOutputStream(WorkbookBuilder.getPath())) {
            workbook = SheetBuilderController.getBudgetTracker();
            workbook.write(fio);
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,SAVE_ERROR,ioe);
        }
    }

    /**
     * Method sets predefined cell design
     * for cells which will contain some value.
     * @param cellStyle new cell style
     */
    private void setDefaultDesign(final CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(CALIBRI_FONT);
        font.setFontHeight(fontSize);
        cellStyle.setFont(font);
    }

    /**
    * Method sets predefined cell design header section.
    * @param cellStyle new cell style
    */
    private void setCommonHeaderDesign(final CellStyle cellStyle) {
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setBold(true);
        font.setFontName(CALIBRI_FONT);
        font.setColor(IndexedColors.WHITE.index);
        font.setFontHeight(fontSize);
        cellStyle.setFont(font);
    }

    /**
     * Method sets design for all headers nor the TOTAL column.
     * @param cellStyle new cell style
     */
    private void setDefaultHeader(final CellStyle cellStyle) {
        cellStyle.setFillForegroundColor(IndexedColors.BLACK.index);
        setCommonHeaderDesign(cellStyle);
    }

    /**
     * Method sets design for TOTAL column header.
     * @param cellStyle new cell style
     */
    private void setTotalHeader(final CellStyle cellStyle) {
        cellStyle.setFillForegroundColor(IndexedColors.DARK_RED.index);
        setCommonHeaderDesign(cellStyle);
    }

    /**
     * Initiate method for creating new column on user requirement
     * in accordance to predefined design pattern.
     * @param columnName new column name
     */
    public void createNewColumn(final String columnName) {
        Sheet actualSheet = SheetBuilderController.getActualSheet();
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        calcSheetHeight(actualSheet);
        remapTotalColumn(actualSheet, sheetWidth, columnName);
        recalculateTotal(actualSheet);
        saveProgress();
    }

    /**
     * Method secures proper deletion and remapping of given column.
     * @param columnName name of column which is going to be deleted
     */
    public void deleteColumn(final String columnName) {
        Sheet actualSheet = SheetBuilderController.getActualSheet();
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        int indexOfDeletedColumn = 0;

        for (int i = 0; i < sheetWidth; i++) {
            if (columnName.equals(actualSheet.getRow(0).getCell(i).getStringCellValue())) {
                indexOfDeletedColumn =
                        actualSheet.getRow(0).getCell(i).getColumnIndex();
            }
        }
        eraseColumnByIndex(indexOfDeletedColumn, actualSheet);
        remapColumnAfterErasing(indexOfDeletedColumn, actualSheet);
        saveProgress();
    }

    /**
     * Method on given index erase given column data.
     * @param columnIndex index of column which will be erased
     * @param actualSheet actual sheet name
     */
    private void eraseColumnByIndex(final int columnIndex, final Sheet actualSheet) {
        for (int i = 0; i < sheetHeight; i++) {
            actualSheet.getRow(i).
                    removeCell(actualSheet.getRow(i).getCell(columnIndex));
        }
    }

    /**
     * Method secures proper remapping after column deletion.
     * @param columnIndex index of column which was erased
     * @param actualSheet actual sheet name
     */
    private void remapColumnAfterErasing(final int columnIndex, final Sheet actualSheet) {
        sheetWidth = actualSheet.getRow(0).getLastCellNum();
        workbook = SheetBuilderController.getBudgetTracker();
        calcSheetHeight(actualSheet);
        CellStyle cellStyle = workbook.createCellStyle();
        CellStyle headerTotal = workbook.createCellStyle();
        setDefaultDesign(cellStyle);
        for (int i = columnIndex + 1; i < sheetWidth; i++) {
            for (int j = 0; j < sheetHeight; j++) {
                Cell newCell = actualSheet.getRow(j).createCell(i - 1);
                newCell.setCellValue(actualSheet.
                        getRow(j).getCell(i).getStringCellValue());

                if (i == sheetWidth - 1 && j == 0) {
                    setTotalHeader(headerTotal);
                    newCell.setCellStyle(headerTotal);
                }
                if (i == sheetWidth - 1 && j > 0) {
                    newCell.setCellStyle(cellStyle);
                }
            }
            eraseColumnByIndex(i, actualSheet);
        }
        recalculateTotal(actualSheet);
    }

    /**
     * Method recalculates values int TOTAL column whenever
     * action in sheet is performed.
     * @param actualSheet actual sheet name
     */
    private void recalculateTotal(final Sheet actualSheet) {
        double sum = 0;

        for (int i = 1; i < sheetHeight; i++) {
            int lastCell = actualSheet.getRow(0).getLastCellNum() - 1;
            for (int j = 1; j < lastCell; j++) {
                sum += Double.
                        parseDouble(actualSheet.getRow(i).
                                getCell(j).getStringCellValue());
            }
            actualSheet.
                    getRow(i).getCell(actualSheet.getRow(0).
                            getLastCellNum() - 1).
                            setCellValue(String.format(CZECH_CURR_FORMAT, sum));
            sum = 0;
        }
    }
}
