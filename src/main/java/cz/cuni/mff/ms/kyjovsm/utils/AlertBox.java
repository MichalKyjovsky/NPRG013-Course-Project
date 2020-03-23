package cz.cuni.mff.ms.kyjovsm.utils;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertBox {

    /**
     * A constant representing infix for calling AlertBoxEmptyInput.fxml.
     */
    public static final String ALERT_BOX_EMPTY_INPUT = "AlertBoxEmptyInput";
    /**
     * A constant representing infix
     * for calling AlertBoxNotImplementedFeature.fxml.
     */
    public static final String ALERT_BOX_NOT_IMPLEMENTED_FEATURE
                                = "AlertBoxNotImplementedFeature";
    /**
     * A constant representing infix for calling AlertBoxInvalidInput.fxml.
     */
    public static final String ALERT_BOX_INVALID_INPUT = "AlertBoxInvalidInput";
    /**
     * Scene instance of the class AlertBox.
     * This variables secures displaying of particular Alert box content.
     */
    private Scene alertBoxScene;
    /**
     * Stage instance of the class AlertBox
     * which enable display particular Alert box on user's screen.
     */
    private Stage alertBoxStage;

    /**
     * A Label displaying error message given as a parameter.
     */
    @FXML
    private Label errorMessage;
    /**
     * Button instance for proper closure of the Alert Box window.
     */
    @FXML
    private Button closeButton;
    /**
     * Variable verify that all precondition for continuing
     * to the Sheet.fxml was fulfilled. If not, Reload of the
     * LandingPage.fxml is performed.
     */
    private static boolean status = false;
    /**
     * An instance of Tools class, which provides method
     * for loading .fxml files to the instance of Scene.
     */
    private final Tools tool = new Tools();

    /**
     * Error message raised whenever loading FXML into
     * Scene instance failed.
     */
    private static final String FXML_LOAD_ERROR =
            "FXML was not loaded into Scene.";

    public static final String ALERT_BOX_IMMUTABLE_FIELDS =
            "AlertBoxImmutableField";

    /**
     * An instance of class logger for creating debugging log messages.
     */
    private Logger logger = Logger.getLogger(AlertBox.class.getName());


    /**
     * Method enable to obtain current instance of displayed AlertBox button,
     * which enable user to get instance of Stage and set up event handlers.
     * @return Instance of shown button in the Alert Box window.
     */
    public Button getCloseButton() {
        return closeButton;
    }

    /**
     * Method enable to obtain current instance of displayed AlertBox Stage,
     * which enable user to set up event handlers.
     * @return Instance of shown Alert Box stage.
     */
    public Stage getAlertBoxStage() {
        return alertBoxStage;
    }


    /**
     * Method will invoke Alert box
     * that user's input is incorrect in certain way.
     * @param errorStatus parameter which involve what message will be shown.
     */
    public  void displayAlertBox(final String errorStatus) {
        alertBoxStage = new Stage();
        try {
            switch (errorStatus) {
                case ALERT_BOX_EMPTY_INPUT:
                    alertBoxScene = new Scene(loadFXML(ALERT_BOX_EMPTY_INPUT));
                    status = true;
                    break;
                case ALERT_BOX_NOT_IMPLEMENTED_FEATURE:
                    alertBoxScene =
                            new Scene(loadFXML(ALERT_BOX_NOT_IMPLEMENTED_FEATURE));
                    break;
                case ALERT_BOX_INVALID_INPUT:
                    alertBoxScene = new Scene(loadFXML(ALERT_BOX_INVALID_INPUT));
                    status = true;
                    break;
                case ALERT_BOX_IMMUTABLE_FIELDS:
                    alertBoxScene = new Scene(loadFXML(ALERT_BOX_IMMUTABLE_FIELDS));
                    status = true;
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, FXML_LOAD_ERROR, e);
        }
        alertBoxStage.setResizable(false);
        alertBoxStage.setScene(alertBoxScene);
        alertBoxStage.show();
    }


    /**
     * Method will secure proper closure of Alert box.
     */
    public void closeAlertBox() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        Scene reload = null;
        try {
            String relatedFxmlLandingPage = "ui/LandingPage.fxml";
            String landingPageControllerClassName =
                    LandingPageController.class.getName();
            reload = new Scene(tool.loadFXML(Class.
                    forName(landingPageControllerClassName),
                    relatedFxmlLandingPage));
        } catch (Exception e) {
            logger.log(Level.SEVERE, FXML_LOAD_ERROR, e);
        }
        stage.close();
        if (!status) {
            App.changeScene(reload);
        }
    }


    private Parent loadFXML(final String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlertBox.class.getClassLoader().
                getResource("additionalUtils/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
