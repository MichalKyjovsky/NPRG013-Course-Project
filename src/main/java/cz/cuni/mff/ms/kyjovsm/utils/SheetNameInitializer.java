package cz.cuni.mff.ms.kyjovsm.utils;

import cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
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

public class SheetNameInitializer {

    /**
     * MenuButton instance providing selection of initial tracking month.
     */
    @FXML
    private MenuButton selectMonthButton;
    /**
     * Button instance providing OnClick action
     * submit the name value of the sheet.
     */
    @FXML
    private Button submitButton;
    /**
     * InputLine instance enabling user to write
     * new column name.
     */
    @FXML
    private TextField inputLine;
    /**
     * An instance of Stage class, which boarders
     * all Dialog Window elements (Shown Dialog Window).
     */
    private Stage dialogWindow;
    /**
     * Full class name of the class SheetNameInitializer.
     */
    private final String sheetNameInitializerClassName =
            SheetNameInitializer.class.getName();
    /**
     * Instance of the AlertBox class, enables
     * to call its methods whenever wrong input
     * is provided.
     */
    private AlertBox alertBox;
    /**
     * Instance of the SheetBuilder class, which provides
     * functions to work with selected sheet.
     */
    private SheetBuilder sheetBuilder;

    /**
     * Error message when obtaining instance of current shown
     * Stage was unsuccessful.
     */
    private static final String STAGE_REFERENCE_ERROR =
            "Reference to the displayed Stage was not working";

    private static final String ALLOWED_CHARS_REGEX =
            "[a-zA-z][a-zA-Z0-9_-]*";

    /**
     * Instance of class Logger for creating
     * logs for debugging purposes.
     */
    private static Logger logger =
            Logger.getLogger(SheetNameInitializer.class.getName());

    /**
     * Error message when FXML loading into Scene instance
     * was unsuccessful.
     */
    private static final String FXML_LOAD_ERROR = "FXML was not loaded into Scene.";

    /**
     * The constructor method.
     */
    public SheetNameInitializer() {
        sheetBuilder = new SheetBuilder();
        alertBox = new AlertBox();
        dialogWindow = new Stage();
    }

    /**
     * @return instance of Stage class of current shown dialogWindow.
     */
    public Stage getDialogWindow() {
        return dialogWindow;
    }


    /**
     * Based on user input method will initiate
     * new tracking month for given input and
     * new sheet based on the given information will be created.
\     */
    public void setNewTrackingMonth() {
        Tools tool = new Tools();
        dialogWindow.setResizable(false);
        String relatedFxmlSheet = "additionalUtils/SheetNameInitializer.fxml";
        try {
             dialogWindow.setScene(new Scene(tool.
                     loadFXML(Class.forName(sheetNameInitializerClassName),
                             relatedFxmlSheet)));
        } catch (Exception e) {
            logger.log(Level.SEVERE,FXML_LOAD_ERROR,e);
        }
        dialogWindow.show();
    }


    /**
     * When user press "ADD NEW COLUMN" method starts initiation process.
     */
    public void addNewColumn() {
        dialogWindow.setResizable(false);
        Tools tool = new Tools();
        String relatedFxmlColumn = "additionalUtils/ColumnNameInitializer.fxml";
        try {
            dialogWindow.setScene(new Scene(tool.
                    loadFXML(Class.forName(sheetNameInitializerClassName),
                            relatedFxmlColumn)));
        } catch (Exception e) {
            logger.log(Level.SEVERE,FXML_LOAD_ERROR,e);
        }
        dialogWindow.show();
    }

    /**
     * When a new tracking month is provided by user, method
     * wul then secure new sheet initiation and creation.
     */
    @FXML
    private void submitMonth() {
        List<MenuItem> menuItems = selectMonthButton.getItems();
        Next:
        for (MenuItem mi : menuItems) {
            for (int i = 0; i < SheetBuilderController.getBudgetTracker().getNumberOfSheets(); i++) {
                if (SheetBuilderController.getBudgetTracker().getSheetAt(i).getSheetName().contains(mi.getText().toUpperCase())) {
                    continue Next;
                }
            }

            mi.setOnAction(e -> {
                int index = menuItems.indexOf(mi) + 1;
                try {
                    Stage stage =
                            (Stage) selectMonthButton.getScene().getWindow();
                    sheetBuilder.createNewSheet(index);
                    stage.close();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, STAGE_REFERENCE_ERROR, ex);
                }
            });
        }
    }

    /**
     * When name of new column is provided by user, new column is then
     * initiated and refactoring is performed.
     */
    @FXML
    private void submitName() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        String newColumnName = inputLine.getCharacters().toString();

        if (newColumnName.isBlank() || newColumnName.isEmpty()) {
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
        } else if (!newColumnName.matches(ALLOWED_CHARS_REGEX)) {
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
        } else {
            sheetBuilder.createNewColumn(newColumnName);
            stage.close();
        }
    }
}
