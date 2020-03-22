package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.utils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.utils.Tools;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.util.NotImplemented;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LandingPageController {

    /**
     * User home directory, OS independent.
     */
    private static final String USER_HOME = "user.home";

    /**
     * Button class instance which on action creates
     * new initial workbook.
     */
    @FXML
    private Button createNewWorkbookButton;
    /**
     * Button class instance which on action invoke
     * FileChooser dialog to load file from local device.
     */
    @FXML
    private Button openFromLocalButton;
    /**
     * Button class instance which on action invoke
     * dialog enabling user fetch file from his personal
     * OneDrive. Not implemented YET.
     */
    @FXML
    private Button openFromCloudButton;

    /**
     * Desired file format suffix.
     */
    private static final String FILE_SUFFIX = ".xlsx";

    /**
     * Error message when obtaining instance of current shown
     * Stage was unsuccessful.
     */
    private static final String STAGE_REFERENCE_ERROR =
            "Reference to the displayed Stage was not working";


    /**
     * An instance of class logger for creating debugging log messages.
     */
    private Logger logger =
            Logger.getLogger(LandingPageController.class.getName());


    /**
     * Method called when "Create New Workbook" button is pressed.
     * It will change the scene of the application and create the
     * new workbook in xlsx format
     */
    public void createNewWorkbook() {
        WorkbookController wc = new WorkbookController();
        try {
            wc.createWorkbook();
            disableButtonsOnClick(true);
            Stage front = wc.getElement();
            front.setOnCloseRequest(e -> disableButtonsOnClick(false));
        } catch (Exception e) {
            logger.log(Level.SEVERE,STAGE_REFERENCE_ERROR);
        }
    }


    /**
     * Method enables to user chose Open file from local device option.
     */
    public void displayFileExplorer() {
        Stage fileDialog = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty(USER_HOME)));
        Tools tool = new Tools();
        String relatedFxmlSheet = "ui/Sheet.fxml";
        try {
            disableButtonsOnClick(true);
            File chosenFile = fileChooser.showOpenDialog(fileDialog);
            if (chosenFile != null) {
                SheetBuilder.setNameOfTheDocument(chosenFile.toString());
                String landingPageControllerClassName =
                        LandingPageController.class.getName();
                String pathToFile;

                if (!chosenFile.toString().endsWith(FILE_SUFFIX)) {
                    pathToFile = chosenFile.toString() + FILE_SUFFIX;
                } else {
                    pathToFile = chosenFile.toString();
                }

                WorkbookBuilder wb = new WorkbookBuilder();
                wb.createFromExistingFile(pathToFile);
                App.changeScene(new Scene(tool.
                        loadFXML(Class.forName(landingPageControllerClassName),
                                relatedFxmlSheet)));
            } else {
                disableButtonsOnClick(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * When AlertBoxes are invoked, all button on background are disabled.
     * when method is called with true parameter
     * @param status defines whether element will be disabled - true
     *               or enabled - false
     */
    private void disableButtonsOnClick(final boolean status) {
        createNewWorkbookButton.setDisable(status);
        openFromLocalButton.setDisable(status);
        openFromCloudButton.setDisable(status);
    }


    /**
     * Method will invoke OneDrive dialog via Microsoft graphs
     * to fetch file directly from OneDrive.
     */
    @NotImplemented
    public void openFromCloud() {
        AlertBox alertBox = new AlertBox();
        alertBox.displayAlertBox(AlertBox.ALERT_BOX_NOT_IMPLEMENTED_FEATURE);
        Stage frontPage =  alertBox.getAlertBoxStage();
        if (frontPage.isShowing()) {
            disableButtonsOnClick(true);
        }
        frontPage.setOnCloseRequest(e -> disableButtonsOnClick(false));
    }
}
