package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
<<<<<<< HEAD
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
=======
>>>>>>> master
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LandingPageController {

<<<<<<< HEAD
    public Button getCreateNewWorkbookButton() {
        return createNewWorkbookButton;
    }

    public Button getOpenFromLocalButton() {
        return openFromLocalButton;
    }

    public Button getOpenFromCloudButton() {
        return openFromCloudButton;
    }

    @FXML
    private Button createNewWorkbookButton;
    @FXML
    private Button openFromLocalButton;
    @FXML
    private Button openFromCloudButton;
    private static File chosenFile;

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

    public void displayFileExplorer(){
        Stage fileDialog = new Stage();
        FileChooser fileChooser = new FileChooser();

        try {
            disableButtonsOnClick(true);
            chosenFile = fileChooser.showOpenDialog(fileDialog);
            if (chosenFile != null) {
                App.changeScene(new Scene(loadFXMLforSheet("Sheet")));
                System.out.println(chosenFile);
            }else {
                disableButtonsOnClick(false);
            }
        }catch (Exception e){
            System.err.println("FXML load for opening from local has failed. Check it out!");
        }

    }
=======

>>>>>>> master

    private Parent loadFXMLforSheet(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetBuilderController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static File getChosenFile() {
        return chosenFile;
    }

    public void disableButtonsOnClick(boolean status){
        createNewWorkbookButton.setDisable(status);
        openFromLocalButton.setDisable(status);
        openFromCloudButton.setDisable(status);

    }

    public void openFromCloud(){
        //TODO:Implement in feature releases, not in actual scope.
        AlertBox alertBox = new AlertBox();
        alertBox.displayAlertBox(AlertBox.ALERT_BOX_NOT_IMPLEMENTED_FEATURE);
        Stage frontPage = (Stage) alertBox.getAlertBoxStage().getScene().getWindow();
        if (frontPage.isShowing()){
            disableButtonsOnClick(true);
        }
        frontPage.setOnCloseRequest(e -> {
            disableButtonsOnClick(false);
            //TODO: finisch OK clossing button
        });

    }
}
