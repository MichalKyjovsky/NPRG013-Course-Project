package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import java.io.IOException;

import javafx.scene.control.Button;

public class LandingPageController {

    public Button createNewWorkbookButton;
    public Button openFromLocalButton;

    /**
     * Method called when "Create New Workbook" button is pressed.
     * It will change the scene of the application and create the
     * new workbook in xlsx format
     */
    public void createNewWorkbook(){
        WorkbookBuilder wb = new WorkbookBuilder();
        try {
            wb.createWorkbook();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void displayFileExplorer(){
        AlertBox alertBox = new AlertBox();
        alertBox.displayAlertBox("AlertBoxEmptyInput");
    }

}
