package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkbookBuilder {

    /**
     * Instance of class Workbook, acting like an reference,
     * to the actual handling workbook.
     */
    private Workbook workbook;
    /**
     * Instance of LocalDateTime class providing current time.
     */
    private LocalDateTime ldt;
    /**
     * Variable storing the full path to the document.
     */
    private static  String  path;
    /**
     * Integer variable storing value of initial month.
     */
    private static int initialMonth;
    /**
     * File name prefix.
     */
    private static final String SHEET_PREFIX = "Budget_";
    /**
     * File name separator.
     */
    private static final String SEPARATOR = "_";
    /**
     * User home directory, OS independent.
     */
    private static final String USER_HOME_DIR = "user.home";
    /**
     * Default column width.
     */
    private static final int COLUMNS_WIDTH = 4000;
    /**
     * Default header cells height.
     */
    private static final int HEADER_HEIGHT = 40;
    /**
     * Heading String for TOTAL column.
     */
    private static final String TOTAL_HEADING = "TOTAL";
    /**
     * Heading String for DATE column.
     */
    private static final String DATE_HEADING = "DATE";
    /**
     * Date format String.
     */
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    /**
     * Account format String.
     */
    private static final String ACCOUNT_FORMAT = "#,##0.00";
    /**
     * Logger instance for logging status of application execution.
     */
    private final Logger logger =
            Logger.getLogger(WorkbookBuilder.class.getName());
    /**
     * Calibri font name constant.
     */
    private static final String CALIBRI_FONT = "Calibri";
    /**
     * Integer constant of default row height.
     */
    private static final int ROW_HEIGHT = 15;
    /**
     * Integer constant storing default font size.
     */
    private static final int FONT_SIZE = 11;
    /**
     * Integer constant to recognition if months number
     * are one or two digits.
     */
    private static final int TWO_DIGIT_BOUND = 10;
    /**
     * @return full path to the file on local disk
     */
    public static String getPath() {
        return path;
    }
    /**
     * Method for setting Initial Month.
     * @param month initial tracking month
     */
    public static void setInitialMonth(final int month) {
        WorkbookBuilder.initialMonth = month;
    }
    /**
     * Method will load particular xlsx sheet into the XSSDWorkbook instance.
     * @param pathToFile file location of xlsx sheet
     */
    public void createFromExistingFile(final String pathToFile) {
        workbook = null;
        path = pathToFile;
        try (FileInputStream fis = new FileInputStream(new File(pathToFile))) {
            SheetBuilderController.setBudgetTracker(new XSSFWorkbook(fis));
            SheetBuilderController.setActualSheet(SheetBuilderController.
                    getBudgetTracker().getSheetAt(0));
            SheetBuilderController.setActualColumn(SheetBuilderController.
                    getActualSheet().getRow(0).getCell(0).getStringCellValue());
            SheetBuilderController.setActualRow(SheetBuilderController.
                    getActualSheet().getRow(0).getCell(1).getStringCellValue());

            logger.log(Level.INFO, "Workbook successfully opened");
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,
                    "Workbook has been opened successfully.",
                    ioe);
        }
    }
    /**
     * Method for creating initial unified Workbook.
     * @return Workbook instance prepared
     * to work with in accordance to predefined pattern.
     */
    public Workbook createInitialWorkbook() {

        workbook = null;
        File currDir = new File(System.getProperty(USER_HOME_DIR));
        path =
                currDir.getAbsolutePath()
                        + File.separator
                        + SheetBuilder.getNameOfTheDocument();

        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            workbook = new XSSFWorkbook();
            ldt = LocalDateTime.now();
            Sheet sheet01 =
                    workbook.createSheet(SHEET_PREFIX
                            + new DateFormatSymbols().
                            getMonths()[initialMonth - 1].toUpperCase()
                            + SEPARATOR + ldt.getYear());
            createInitialSheet(sheet01, initialMonth, this.workbook);
            workbook.write(outputStream);
            logger.log(Level.INFO,
                    "Workbook was successfully initiated.");
        } catch (IOException ioe) {
            logger.log(Level.SEVERE,
                    "Initial workbook has not been created.",
                    ioe);
        }
        return workbook;
    }

    /**
     * Method for Initial Sheets creation in demanded form.
     * @param newSheet new Sheet instance
     * @param month initial selected month
     * @param document new Workbook instance
     */
    void createInitialSheet(final Sheet newSheet,
                            final int month,
                            final Workbook document) {
        ldt = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.of(ldt.getYear(), month);
        newSheet.setDefaultColumnWidth(COLUMNS_WIDTH);
        newSheet.setColumnWidth(0, COLUMNS_WIDTH);
        newSheet.setColumnWidth(1, COLUMNS_WIDTH);
        Row header = newSheet.createRow(0);
        header.setHeightInPoints(HEADER_HEIGHT);
        CellStyle headerStyleBlack = document.createCellStyle();
        CellStyle headerStyleRed = document.createCellStyle();
        setBasicHeader(headerStyleBlack, headerStyleRed);

        XSSFFont font = ((XSSFWorkbook) document).createFont();
        headerStyleBlack.setFont(setFont(font, CALIBRI_FONT));
        headerStyleRed.setFont(setFont(font, CALIBRI_FONT));

        Cell headerCell0 = header.createCell(0);
        headerCell0.setCellValue(DATE_HEADING);
        headerCell0.setCellStyle(headerStyleBlack);

        Cell headerCell1 = header.createCell(1);
        headerCell1.setCellValue(TOTAL_HEADING);
        headerCell1.setCellStyle(headerStyleRed);

        DataFormat format = document.createDataFormat();

        CellStyle styleDate = setBasicCellStyle(document);
        CellStyle styleTotal = setBasicCellStyle(document);
        styleDate.setDataFormat(format.getFormat(DATE_FORMAT));
        styleTotal.setDataFormat(format.getFormat(ACCOUNT_FORMAT));
        int currentMonth = yearMonth.getMonthValue();
        String actualMonth = String.valueOf(currentMonth);
        final int lowerMonthBound = 10;

        if (currentMonth < lowerMonthBound) {
            actualMonth = String.format("0%d", currentMonth);
        }

        for (int i = 1; i < yearMonth.lengthOfMonth() + 1; i++) {
            Row row = newSheet.createRow(i);
            row.setHeightInPoints(ROW_HEIGHT);
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            cell0.setCellStyle(styleDate);
            cell1.setCellStyle(styleTotal);

            if (i < TWO_DIGIT_BOUND) {
                cell0.setCellValue(String.
                        format("%d%d.%s.%d",
                                0, i, actualMonth, ldt.getYear()));
            } else {
                cell0.setCellValue(String.
                        format("%d.%s.%d",
                                i, actualMonth, ldt.getYear()));
            }
            cell1.setCellValue(String.format("%.2f CZK", (double) 0));
        }
        SheetBuilderController.setActualSheet(newSheet);
        SheetBuilderController.setActualColumn(SheetBuilderController.
                getActualSheet().getRow(0).getCell(0).getStringCellValue());
        SheetBuilderController.setActualRow(SheetBuilderController.
                getActualSheet().getRow(0).getCell(1).getStringCellValue());
    }

    /**
     * Method enables to set Cell style common for
     * all non-header cells in the document.
     * @param document reference to current opened Workbook instance.
     * @return basic style for all Cells.
     */
    private CellStyle setBasicCellStyle(final Workbook document) {
        CellStyle style = document.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);

        return style;
    }

    /**
     * Method enables to setup two main header styles,
     * RED meant for TOTAL column and BLACK for others.
     * @param headerStyleBlack
     * @param headerStyleRed
     */
    private void setBasicHeader(final CellStyle headerStyleBlack,
                                final CellStyle headerStyleRed) {
        headerStyleBlack.setFillForegroundColor(IndexedColors.BLACK.index);
        headerStyleRed.setFillForegroundColor(IndexedColors.DARK_RED.index);
        headerStyleBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleBlack.setAlignment(HorizontalAlignment.CENTER);
        headerStyleRed.setAlignment(HorizontalAlignment.CENTER);
    }

    /**
     * Method enables setup of font styles.
     * @param font reference to a selected XSSFFont instance.
     * @param fontStyle Font style-family
     * @return default font style setup
     */
    private XSSFFont setFont(final XSSFFont font, final String fontStyle) {
        font.setFontName(fontStyle);
        font.setColor(IndexedColors.WHITE.index);
        font.setBold(true);
        font.setFontHeight(FONT_SIZE);
        return font;
    }
}
