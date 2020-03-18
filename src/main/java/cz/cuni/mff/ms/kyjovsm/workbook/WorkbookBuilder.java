package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
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
import java.util.logging.Logger;

public class WorkbookBuilder {

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
     * @return full path to the file on local disk
     */
    public static String getPath() {
        return path;
    }

    /**
     * Method for setting Initial Month.
     * @param initialMonth initial tracking month
     */
    public static void setInitialMonth(final int initialMonth) {
        WorkbookBuilder.initialMonth = initialMonth;
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        logger.fine("Workbook successfully opened");
    }

    /**
     * Method for creating initial unified Workbook.
     * @return Workbook instance prepared
     * to work with in accordance to predefined pattern.
     * @throws FileFormatException
     */
    public Workbook createInitialWorkbook() throws FileFormatException {
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
        } catch (IOException ioe) {
            throw new FileFormatException();
        }
        logger.fine("Workbook was successfully initiated and saved to user HOME directory.");
        return workbook;
    }

    /**
     * Method for Initial Sheets creation in demanded form.
     * @param newSheet new Sheet instance
     * @param initialMonth initial selected month
     * @param workbook new Workbook instance
     */
    void createInitialSheet(final Sheet newSheet, final int initialMonth, final Workbook workbook) {
        ldt = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.of(ldt.getYear(), initialMonth);
        newSheet.setDefaultColumnWidth(COLUMNS_WIDTH);
        newSheet.setColumnWidth(0, COLUMNS_WIDTH);
        newSheet.setColumnWidth(1, COLUMNS_WIDTH);
        Row header = newSheet.createRow(0);
        header.setHeightInPoints(HEADER_HEIGHT);
        CellStyle headerStyleBlack = workbook.createCellStyle();
        CellStyle headerStyleRed = workbook.createCellStyle();
        setBasicHeader(headerStyleBlack, headerStyleRed);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        headerStyleBlack.setFont(setFont(font, "Calibri"));
        headerStyleRed.setFont(setFont(font, "Calibri"));

        Cell headerCell0 = header.createCell(0);
        headerCell0.setCellValue(DATE_HEADING);
        headerCell0.setCellStyle(headerStyleBlack);

        Cell headerCell1 = header.createCell(1);
        headerCell1.setCellValue(TOTAL_HEADING);
        headerCell1.setCellStyle(headerStyleRed);

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
            Cell cell0 = row.createCell(0);
            Cell cell1 = row.createCell(1);
            cell0.setCellStyle(style_date);
            cell1.setCellStyle(style_total);

            if (i < 10) {
                cell0.setCellValue(String.format("%d%d.%s.%d", 0, i, actualMonth, ldt.getYear()));
            } else {
                cell0.setCellValue(String.format("%d.%s.%d", i, actualMonth, ldt.getYear()));
            }
            cell1.setCellValue(String.format("%.2f CZK", (double) 0));
        }
    }

    /**
     * @param workbook
     * @return basic style for all Cells.
     */
    private CellStyle setBasicCellStyle(final Workbook workbook) {
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

    /**
     * @param headerStyleBlack
     * @param headerStyleRed
     */
    private void setBasicHeader(final CellStyle headerStyleBlack, final CellStyle headerStyleRed) {
        headerStyleBlack.setFillForegroundColor(IndexedColors.BLACK.index);
        headerStyleRed.setFillForegroundColor(IndexedColors.DARK_RED.index);
        headerStyleBlack.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleRed.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyleBlack.setAlignment(HorizontalAlignment.CENTER);
        headerStyleRed.setAlignment(HorizontalAlignment.CENTER);
    }

    /**
     * @param font
     * @param fontStyle
     * @return default font style setup
     */
    private XSSFFont setFont(final XSSFFont font, final String fontStyle) {
        font.setFontName(fontStyle);
        font.setColor(IndexedColors.WHITE.index);
        font.setBold(true);
        int fontSize = 11;
        font.setFontHeight(fontSize);
        return font;
    }
}
