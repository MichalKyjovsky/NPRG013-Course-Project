package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;

import java.io.File;
import java.io.IOException;

import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void displayFileExplorer() throws FXMLLoaderException{
        Stage fileDialog = new Stage();
        FileChooser fileChooser = new FileChooser();

        try {
            disableButtonsOnClick(true);
            File chosenFile = fileChooser.showOpenDialog(fileDialog);
            if (chosenFile != null) {
                App.changeScene(new Scene(loadSheetXML()));
                SheetBuilder.setNameOfTheDocument(chosenFile.toString());
            }else {
                disableButtonsOnClick(false);
            }
        }catch (Exception e){
            throw new FXMLLoaderException();
        }
    }

    private Parent loadSheetXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetBuilderController.class.getResource("Sheet.fxml"));
        return fxmlLoader.load();
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
