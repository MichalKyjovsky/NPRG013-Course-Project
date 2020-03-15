package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import cz.cuni.mff.ms.kyjovsm.ui.App;
import cz.cuni.mff.ms.kyjovsm.ui.LandingPageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBox {

    public static final String ALERT_BOX_EMPTY_INPUT = "AlertBoxEmptyInput";
    public static final String ALERT_BOX_NOT_IMPLEMENTED_FEATURE = "AlertBoxNotImplementedFeature";
    public static final String ALERT_BOX_INVALID_INPUT = "AlertBoxInvalidInput";
    private Scene alertBoxScene;
    private Stage alertBoxStage;
    @FXML
    Label errorMessage;
    @FXML
    Button closeButton;
    private static boolean status = false;
    private Tools tool = new Tools();
    private String relatedFxmlLandingPage = "ui/LandingPage.fxml";
    private String landingPageControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.LandingPageController";


    public Button getCloseButton(){
        return closeButton;
    }

    public Stage getAlertBoxStage() {
        return alertBoxStage;
    }


    /**
     * Method will invoke alert box that user's input is incorrect in certain way
     * @param errorStatus parameter which involve what message will be shown
     */
    public  void displayAlertBox(String errorStatus){
        alertBoxStage = new Stage();
        try {
            if (errorStatus.equals(ALERT_BOX_EMPTY_INPUT)) {
                alertBoxScene = new Scene(loadFXML(ALERT_BOX_EMPTY_INPUT));
                status = true;
            }
            else if (errorStatus.equals(ALERT_BOX_NOT_IMPLEMENTED_FEATURE)){
                alertBoxScene = new Scene(loadFXML(ALERT_BOX_NOT_IMPLEMENTED_FEATURE));
            }
            else if (errorStatus.equals(ALERT_BOX_INVALID_INPUT)){
                alertBoxScene = new Scene(loadFXML(ALERT_BOX_INVALID_INPUT));
                status = true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        alertBoxStage.setResizable(false);
        alertBoxStage.setScene(alertBoxScene);
        alertBoxStage.show();
    }


    /**
     * Method will secure proper close of Alert box
     */
    public void closeAlertBox(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Scene reload = null;
        try {
            reload = new Scene(tool.loadFXML(Class.forName(landingPageControllerClassName),relatedFxmlLandingPage));
        }catch (Exception e){
            e.printStackTrace();
        }
        stage.close();
        if(!status) {
            App.changeScene(reload);
        }
    }


    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlertBox.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
