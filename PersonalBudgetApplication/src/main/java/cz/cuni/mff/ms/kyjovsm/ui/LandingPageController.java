package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;

import java.io.File;
import java.io.IOException;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LandingPageController {

    @FXML
    private Button createNewWorkbookButton;
    @FXML
    private Button openFromLocalButton;
    @FXML
    private Button openFromCloudButton;

    private String relatedFxmlSheet = "Sheet.fxml";
    private String landingPageControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.LandingPageController";
    private static final String FILE_SUFFIX = ".xlsx";

    /**
     * Method called when "Create New Workbook" button is pressed.
     * It will change the scene of the application and create the
     * new workbook in xlsx format
     */
    public void createNewWorkbook(){
        WorkbookController wc = new WorkbookController();
        try {
            wc.createWorkbook();
            disableButtonsOnClick(true);
            Stage front = wc.getElement();
            front.setOnCloseRequest(e -> {
                disableButtonsOnClick(false);
            });
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public void displayFileExplorer() throws FXMLLoaderException{
        Stage fileDialog = new Stage();
        FileChooser fileChooser = new FileChooser();
        Tools tool = new Tools();
        try {
            disableButtonsOnClick(true);
            File chosenFile = fileChooser.showOpenDialog(fileDialog);
            if (chosenFile != null) {
                SheetBuilder.setNameOfTheDocument(chosenFile.toString());
                App.changeScene(new Scene(tool.loadFXML(Class.forName(landingPageControllerClassName),relatedFxmlSheet)));
                String pathToFile;

                if(!chosenFile.toString().endsWith(FILE_SUFFIX)) {
                    pathToFile = chosenFile.toString() + FILE_SUFFIX;
                }
                else{
                    pathToFile = chosenFile.toString();
                }

                WorkbookBuilder wb = new WorkbookBuilder();
                wb.createFromExistingFile(pathToFile);
            }else {
                disableButtonsOnClick(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            //throw new FXMLLoaderException(relatedFxmlSheet);
        }
    }

    private void disableButtonsOnClick(boolean status){
        createNewWorkbookButton.setDisable(status);
        openFromLocalButton.setDisable(status);
        openFromCloudButton.setDisable(status);
    }

    public void openFromCloud(){
        //TODO:Implement in feature releases, not in actual scope.
        AlertBox alertBox = new AlertBox();
        alertBox.displayAlertBox(AlertBox.ALERT_BOX_NOT_IMPLEMENTED_FEATURE);
        Stage frontPage =  alertBox.getAlertBoxStage();
        if (frontPage.isShowing()){
            disableButtonsOnClick(true);
        }
        frontPage.setOnCloseRequest(e -> {
            disableButtonsOnClick(false);
        });
    }
}
