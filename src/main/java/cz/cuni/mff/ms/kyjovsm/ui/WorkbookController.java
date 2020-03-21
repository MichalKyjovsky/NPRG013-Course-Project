package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.utils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.utils.Tools;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkbookController {
    /**
     * Instance of class MenuButton enabling user to
     * select Initial Tracking month.
     */
    @FXML
    private MenuButton selectMonthButton;
    /**
     * Instance of class Button allowing user
     * to submit chosen name for the Workbook.
     */
    @FXML
    private Button submitButton;
    /**
     * Instance of class TextField providing user
     * a field for typing name of the Workbook.
     */
    @FXML
    private TextField inputField;
    /**
     * Instance of class Stage displaying dialogs
     * necessary for initialization of the Workbook.
     */
    private Stage window;

    private static final String FXML_LOAD_ERROR = "FXML was not loaded into Scene.";

    private static final String ALLOWED_NAME_CHARS =
            "[a-zA-Z0-9][a-zA-Z0-9_ -]*";
    /**
     * Instance of class AlertBox, which displays whenever
     * user provide wrong input.
     */
    private AlertBox alertBox;
    /**
     * Instance of class Scene where content
     * for initializing dialogs is shown.
     */
    private Scene workBookInitializer;
    /**
     * Variable storing full class name necessary for loading
     * the class related FXML file.
     */
    private final String workbookControllerClassName =
            "cz.cuni.mff.ms.kyjovsm.ui.WorkbookController";
    /**
     * Instance of class Tools providing functionality
     * of loading FXML into Scene instance.
     */
    private final Tools tool;

    private static Logger logger =
            Logger.getLogger(WorkbookController.class.getName());

    /**
     * Method on invoked dialog enables to set name of the
     * Workbook which tend to be processed.
     * @param nameOfDoc Name of the Workbook.
     */
    public static void setNameOfDoc(final String nameOfDoc) {
        WorkbookController.nameOfDoc = nameOfDoc;
    }

    /**
     * Variable holding name of the Workbook.
     */
    private static String nameOfDoc;

    /**
     * Constructor method.
     */
    public WorkbookController() {
        tool = new Tools();
    }

    /**
     * Method returning current displayed Stage instance.
     * @return Current showing window.
     */
    public Stage getElement() {
        return this.window;
    }


    /**
     * Method enables user to invoke setup dialog of new Workbook.
     */
    void createWorkbook() {
        String relatedFxmlWorkbook = "ui/Workbook.fxml";
        try {
            window = new Stage();
            workBookInitializer =
                    new Scene(tool.
                            loadFXML(Class.
                                    forName(workbookControllerClassName),
                                    relatedFxmlWorkbook));
            window.setScene(workBookInitializer);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method enables user to setup new Workbook name.
     */
    public void setUpNameOfDocument() {
        create();
    }

    private void create() {
        nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        try {
            submitName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void submitName() {
        String relatedFxmlInitialMonthDialog = "ui/InitialMonthDialog.fxml";
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches(ALLOWED_NAME_CHARS)) {
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
                inputField.setText("");
            } else {
                SheetBuilder.setNameOfTheDocument(nameOfDoc);
                workBookInitializer =
                        new Scene(tool.
                                loadFXML(Class.
                                        forName(workbookControllerClassName),
                                        relatedFxmlInitialMonthDialog));
                stage.setScene(workBookInitializer);
                stage.show();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, FXML_LOAD_ERROR, e);
        }
    }


    /**
     * Method enables to user set up initial tracking month.
     */
    @FXML
    private void setupInitialMonth() {
        List<MenuItem> menuItems = selectMonthButton.getItems();
        String relatedFxmlSheet = "ui/Sheet.fxml";
        String sheetBuilderControllerClassName =
                SheetBuilderController.class.getName();

        for (MenuItem mi : menuItems) {
            mi.setOnAction(e -> {
                int index = menuItems.indexOf(mi) + 1;
                WorkbookBuilder.setInitialMonth(index);
                try {
                    Stage stage = (Stage) selectMonthButton.
                            getScene().getWindow();
                    SheetBuilderController.
                            setBudgetTracker(new WorkbookBuilder().
                                    createInitialWorkbook());
                    App.changeScene(new Scene(tool.
                            loadFXML(Class.
                                    forName(sheetBuilderControllerClassName),
                                    relatedFxmlSheet)));
                    stage.close();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, FXML_LOAD_ERROR, ex);
                }

            });
        }

    }

    /**
     * Method enables to submit value directly by
     * pressing enter key instead of pressing button.
     * @param ae ENTER key pressed action.
     */
    public void enterSubmit(final ActionEvent ae) {
        create();
    }
}

