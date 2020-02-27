package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import cz.cuni.mff.ms.kyjovsm.ui.WorkbookController;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import cz.cuni.mff.ms.kyjovsm.exceptions.WorkbookBuilderException;

import java.io.File;
import java.io.FileOutputStream;

public class WorkbookBuilder {

    private static WorkbookController workbookController;
    private static SheetBuilder sheetBuilder;
    private static SheetBuilderController sheetBuilderController;
    private static Workbook workbook;
    private static final String XLSX_SUFFIX = ".xlsx";

    public Workbook createWorkbook(String workbookName){
        return new XSSFWorkbook();
    }

    //TODO: bind to the file dialog in SheetBuilderController
    public void saveWorkbook(File fileLocation) throws WorkbookBuilderException{
        String path = fileLocation.getAbsolutePath();
        String location = path.substring(0,path.length() - 1) + XLSX_SUFFIX;

        try{
            FileOutputStream outputStream = new FileOutputStream(location);
            workbook.write(outputStream);
            workbook.close(); //TODO: Actually we do not want to close it after save procedure

        }catch (Exception e){
            throw new WorkbookBuilderException(WorkbookBuilderException.FILE_SAVING_FAILED);
        }
    }

    public WorkbookController getWorkbookController() {
        return workbookController;
    }

    public SheetBuilder getSheetBuilder() {
        return sheetBuilder;
    }

    public SheetBuilderController getSheetBuilderController() {
        return sheetBuilderController;
    }

}
