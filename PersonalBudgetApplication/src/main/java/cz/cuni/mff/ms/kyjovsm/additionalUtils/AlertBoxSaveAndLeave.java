package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import cz.cuni.mff.ms.kyjovsm.ui.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBoxSaveAndLeave {

    private Stage alertBox;
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

        Scene alertScene = null;

        try {
            alertScene = new Scene(loadAlertBoxSaveAndLeaveFXML());
        }catch (Exception e){
            e.printStackTrace();
        }

        alertBox.setResizable(false);
        alertBox.setScene(alertScene);
        alertBox.show();
    }

    private Parent loadAlertBoxSaveAndLeaveFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlertBoxSaveAndLeave.class.getResource("AlertBoxSaveAndLeave.fxml"));
        return fxmlLoader.load();
    }

    private Parent loadAppFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LandingPage.fxml"));
        return fxmlLoader.load();
    }

    public void continueToHomePage() {
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        actual.close();
        try {
            App.changeScene(new Scene(loadAppFXML()));
        }catch (Exception e1){
            e1.printStackTrace();
        }
    }

    public void stayAndSave() {
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        actual.close();
    }
}
