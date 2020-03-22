package cz.cuni.mff.ms.kyjovsm.utils;

import cz.cuni.mff.ms.kyjovsm.ui.App;
import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertBoxSaveAndLeave {

    /**
     * Instance of class Logger for creating
     * logs for debugging purposes.
     */
    private static Logger logger =
            Logger.getLogger(AlertBoxSaveAndLeave.class.getName());

    /**
     * An instance of Tools class, which provides method
     * for loading .fxml files to the instance of Scene.
     */
    private Tools tool;
    /**
     * An instance of Stage class which hold
     * all displayed component in a window.
     */
    private Stage alertBox;

     /**
     * An instance of Button class in the alertBox stage,
     * which on click move user back to the LandingPage.fxml.
     */
    @FXML
    private Button continueToHomePageButton;
    /**
     * an instance of Button class in the alertBox stage,
     * which on click close the alert box and current
     * page will be still displayed.
     */
    @FXML
    private Button stayAndSaveButton;

    /**
     * Error message when FXML loading into Scene instance
     * was unsuccessful.
     */
    private static final String FXML_LOAD_ERROR =
            "FXML was not loaded into Scene.";
    /**
     * Error message when obtaining instance of current shown
     * Stage was unsuccessful.
     */
    private static final String STAGE_REFERENCE_ERROR =
            "Reference to the displayed Stage was not working";

    /**
     * @return Button instance of current shown Alert Box.
     */
    public Button getContinueToHomePageButton() {
        return this.continueToHomePageButton;
    }

    /**
     * @return Button instance of current shown Alert Box.
     */
    public Button getStayAndSaveButton() {
        return this.stayAndSaveButton;
    }

    /**
     * @return Stage instance of current shown Alert Box
     */
    public Stage getAlertBox() {
        return alertBox;
    }


    /**
     * Method will display dialog before leaving to home page.
     */
    public void invokeDialog() {
        alertBox = new Stage();
        tool = new Tools();
        Scene alertScene = null;
        try {
            String relatedFxmlAlertBoxSaveAndLeave =
                    "additionalUtils/AlertBoxSaveAndLeave.fxml";
            String alertBoxSaveAndLeaveClassName =
                    AlertBoxSaveAndLeave.class.getName();
            alertScene = new Scene(tool.loadFXML(Class.
                    forName(alertBoxSaveAndLeaveClassName),
                    relatedFxmlAlertBoxSaveAndLeave));
        } catch (Exception e) {
            logger.log(Level.SEVERE, FXML_LOAD_ERROR, e);
        }
        alertBox.setResizable(false);
        alertBox.setScene(alertScene);
        alertBox.show();
    }


    /**
     * Method will display to user landing page, once he
     * decided his work is ended and saved.
     */
    public void continueToHomePage() {
        tool = new Tools();
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        actual.close();
        try {
            String relatedFxmlLandingPage = "ui/LandingPage.fxml";
            String mainClassName = App.class.getName();
            SheetBuilderController.getBudgetTracker().close();
            App.changeScene(new Scene(tool.loadFXML(
                    Class.forName(mainClassName), relatedFxmlLandingPage)));
        } catch (Exception e) {
            logger.log(Level.SEVERE, FXML_LOAD_ERROR, e);
        }
    }


    /**
     * Method will close dialog and user remain in core.
     */
    public void stayAndSave() {
        Stage actual = (Stage) continueToHomePageButton.getScene().getWindow();
        try {
            SheetBuilderController.getBudgetTracker().close();
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, STAGE_REFERENCE_ERROR, ioe);
        }
        actual.close();
    }
}
