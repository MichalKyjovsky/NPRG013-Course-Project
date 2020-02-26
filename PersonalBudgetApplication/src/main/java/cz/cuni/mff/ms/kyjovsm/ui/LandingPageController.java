package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingPageController {

    @FXML
    public Button createNewWorkbookButton;
    public Button openFromLocalButton;

    /**
     * Method called when "Create New Workbook" button is pressed.
     * It will change the scene of the application and create the
     * new workbook in xlsx format
     */
    public void createNewWorkbook(){
        WorkbookController wc = new WorkbookController();
        try {
            //TODO: during pop-ups windows make all other Button disabled
            wc.createWorkbook();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void displayFileExplorer(){
        AlertBox alertBox = new AlertBox();
        alertBox.displayAlertBox("AlertBoxEmptyInput");
    }

}
