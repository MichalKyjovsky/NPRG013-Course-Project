package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.workbook.workbookBuilder;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class landingPageController {

    public Button createNewWorkbookButton;

    /**
     * Method called when "Create New Workbook" button is pressed.
     * It will change the scene of the application and create the
     * new workbook in xlsx format
     */
    public void createNewWorkbook(){
        workbookBuilder wb = new workbookBuilder();
        try {
            wb.createWorkbook();
        }catch (IOException e){
            e.printStackTrace();
        }
    };

}
