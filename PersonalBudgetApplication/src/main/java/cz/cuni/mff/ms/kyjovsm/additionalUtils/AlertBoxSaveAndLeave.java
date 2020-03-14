package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import cz.cuni.mff.ms.kyjovsm.ui.App;
import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBoxSaveAndLeave {

    private Tools tool;
    private Stage alertBox;
    private String relatedFxmlAlertBoxSaveAndLeave = "AlertBoxSaveAndLeave.fxml";
    private String mainClassName = "cz.cuni.mff.ms.kyjovsm.ui.App";
    private String relatedFxmlLandingPage = "LandingPage.fxml";
    private String alertBoxSaveAndLeaveClassName = "cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave";
    @FXML
    private Button continueToHomePageButton;
    @FXML
    private Button stayAndSaveButton;

    public Button getContinueToHomePageButton(){
        return this.continueToHomePageButton;
    }

    public Button getStayAndSaveButton() {
        return this.stayAndSaveButton;
    }

    public Stage getAlertBox() {
        return alertBox;
    }

    public void invokeDialog(){
        alertBox = new Stage();
        tool = new Tools();
        Scene alertScene = null;

        try {
            alertScene = new Scene(tool.loadFXML(Class.forName(alertBoxSaveAndLeaveClassName),relatedFxmlAlertBoxSaveAndLeave));
        }catch (Exception e){
            e.printStackTrace();
        }

        alertBox.setResizable(false);
        alertBox.setScene(alertScene);
        alertBox.show();
    }



    public void continueToHomePage() {
        tool = new Tools();
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        actual.close();
        try {
            App.changeScene(new Scene(tool.loadFXML(Class.forName(mainClassName),relatedFxmlLandingPage)));
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    public void stayAndSave() {
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        try {
            SheetBuilderController.getBudget_tracker().close();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
        actual.close();
    }
}
