package cz.cuni.mff.ms.kyjovsm.workbook;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SheetBuilder{
    public Sheet createSheet(Workbook workbook, String nameOfSheet){
        return  workbook.createSheet(nameOfSheet);

    }
}
