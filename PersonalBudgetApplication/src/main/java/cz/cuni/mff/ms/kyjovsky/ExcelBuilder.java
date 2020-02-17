package cz.cuni.mff.ms.kyjovsky;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class ExcelBuilder {

    public Workbook returnSheet(String path) {
        try (FileInputStream fis = new FileInputStream(new File(path))){
            Workbook workbook = new XSSFWorkbook(fis);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null; //TODO: finish initializing of worksheet
    }

    private Sheet retrieveSheet(int sheetNumber){
        
        return null; //TODO: finish correct returning
    }
}
