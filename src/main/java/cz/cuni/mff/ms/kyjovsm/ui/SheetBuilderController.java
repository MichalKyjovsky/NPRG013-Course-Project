package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SheetBuilderController implements Initializable {
    /**
     * Instance of class Label providing displaying of current cell.
     */
    @FXML
    private Label displayLabel;
    /**
     * Instance of class Label displaying actual selected row.
     */
    @FXML
    private  Label selectedRowLabel;
    /**
     * Instance of class Label displaying actual selected column.
     */
    @FXML
    private  Label selectedColumnLabel;
    /**
     * Instance of class Label displaying actual selected sheet.
     */
    @FXML
    private  Label selectedSheetLabel;
    /**
     * Instance of class MenuButton allowing
     * to select actual row to which user can set value.
     */
    @FXML
    private MenuButton rowSelectButton;
    /**
     * Instance of class MenuButton allowing
     * to select actual column to which user can set value.
     */
    @FXML
    private MenuButton columnSelectButton;
    /**
     * Instance of class MenuButton
     * allowing to select actual sheet to which user can set value.
     */
    @FXML
    private MenuButton sheetSelectButton;
    /**
     * Instance of class Button enabling
     * user to submit value desired to be written
     * in the cell.
     */
    @FXML
    private Button submitValueButton;
    /**
     * Instance of class button allowing user
     * to "Save As" his progress.
     */
    @FXML
    private Button saveButton;
    /**
     * Instance of class Button allowing user to
     * on click action add new column to the actual sheet.
     */
    @FXML
    private Button addColumnButton;
    /**
     * Instance of class Button allowing user to
     * on click action add new sheet to the opened workbook.
     */
    @FXML
    private Button addSheetButton;
    /**
     * Instance of class Button allowing user to
     * on click action go to the home page.
     */
    @FXML
    private Button homeButton;
    /**
     * Instance of class Button allowing user to
     * on click action delete selected column.
     */
    @FXML
    private Button deleteColumnButton;
    /**
     * Instance of class TextField enabling user
     * input desired values into the sheet.
     */
    @FXML
    private TextField valueInputField;
    /**
     * Instance of class SheetBuilder which provides
     * functionality necessary for editing sheet and
     * keep desired form of the workbook.
     */
    private final SheetBuilder sheetBuilder = new SheetBuilder();
    /**
     * Variable holding current opened Workbook.
     */
    private static Workbook budgetTracker;

    public static void setActualSheet(Sheet actualSheet) {
        SheetBuilderController.actualSheet = actualSheet;
    }

    public static void setActualColumn(String actualColumn) {
        SheetBuilderController.actualColumn = actualColumn;

        for (Cell header: actualSheet.getRow(0)) {
            if (header.getStringCellValue().equals(actualColumn)) {
                SheetBuilderController.actualColumnIndex =
                        header.getColumnIndex();
            }
        }
    }

    public static void setActualRow(String actualRow) {
        SheetBuilderController.actualRow = actualRow;

        int maxHeight = 35;
        try {
            for (int i = 0; i < maxHeight; i++) {
                if (actualRow.equals(actualSheet.getRow(i).getCell(0).getStringCellValue())) {
                    actualRowIndex = i;
                    break;
                }
            }
        } catch (Exception e) { }
    }

    /**
     * Reference to the actual selected sheet.
     */
    private static Sheet actualSheet;
    /**
     * Reference to the actual selected column.
     */
    private static String actualColumn;
    /**
     * Reference to the actual selected row.
     */
    private static String actualRow;
    /**
     * Variable storing integer index of current selected column.
     */
    private static int actualColumnIndex;
    /**
     * Variable storing integer index of current selected row.
     */
    private static int actualRowIndex;

    private static final String USER_HOME = "user.home";

    /**
     * Instance of class Logger enabling easier tracking
     * and debugging, which is documented in generated log file.
     */
    private final Logger logger = Logger.getLogger(SheetBuilderController.class.getName());

    /**
     * Method returns reference to the current sheet if
     * another class request for this information.
     * @return reference to the actual selected sheet.
     */
    public static Sheet getActualSheet() {
        return SheetBuilderController.actualSheet;
    }

    /**
     * Method enable to set current editing Workbook.
     * @param tracker initialized Workbook
     *                created/loaded from WorkbookBuilder class.
     */
    public static void setBudgetTracker(final Workbook tracker) {
        SheetBuilderController.budgetTracker = tracker;
    }

    /**
     * Method for obtaining reference to the current Workbook.
     * @return current opened Workbook.
     */
    public static Workbook getBudgetTracker() {
        return budgetTracker;
    }


    /**
     * Method which is called when home button is pressed.
     * It will invoke dialog if current work is saved
     * and eventually move user to Landing Page.
     */
    public void goToHomePage() {
        AlertBoxSaveAndLeave alertBoxSaveAndLeave = new AlertBoxSaveAndLeave();
        disableAllElements(true);
        alertBoxSaveAndLeave.invokeDialog();
        Stage frontStage = alertBoxSaveAndLeave.getAlertBox();

        frontStage.setOnCloseRequest(e -> disableAllElements(false));
        frontStage.setOnHidden(e -> disableAllElements(false));
    }


    /**
     * Method is called whenever is Alert Box or Dialog box invoked
     * and it is necessary to disable all other buttons.
     * @param statement defines whether elements on background are
     *                  disabled - true
     *                  enabled - false
     */
    private void disableAllElements(final boolean statement) {
        homeButton.setDisable(statement);
        submitValueButton.setDisable(statement);
        saveButton.setDisable(statement);
        addColumnButton.setDisable(statement);
        addSheetButton.setDisable(statement);
        deleteColumnButton.setDisable(statement);
        rowSelectButton.setDisable(statement);
        sheetSelectButton.setDisable(statement);
        columnSelectButton.setDisable(statement);
        valueInputField.setDisable(statement);
    }


    /**
     * When SAVE button is pressed, method will invoke
     * FileChooser dialog instance and user is then able
     * to save his work.
     * @throws FileFormatException
     */

    public void saveDocument() throws FileFormatException {
        FileChooser fileChooser = new FileChooser();

        String[] path = SheetBuilder.getNameOfTheDocument().split("\\\\");
        String file = path[path.length  -1 ];
        fileChooser.setInitialFileName(file);
        fileChooser.setInitialDirectory(new File(System.getProperty(USER_HOME)));

        File fileToSave = fileChooser.showSaveDialog(new Stage());

        try (FileOutputStream fio = new FileOutputStream(fileToSave)) {
            budgetTracker.write(fio);
            logger.log(Level.INFO,"Workbook was successfully saved.");
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "File saving failed",ioe);
        }
    }

    /**
     * Method sends given input if correct to the particular cell
     * whenever is SUBMIT button pressed.
     */
    public void sendValueToCell() {

        if (valueInputField.getCharacters().toString().matches("[0-9+\\-*/()\\s]+")) {
            sheetBuilder.
                    setCellValue(valueInputField.getCharacters().toString(),
                    actualSheet, actualColumnIndex, actualRowIndex);
            logger.log(Level.INFO,
                    "Value cell has been successfully updated.");
        } else {
            AlertBox alertBox = new AlertBox();
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
            disableAllElements(true);
            Stage stage = alertBox.getAlertBoxStage();
            stage.setOnHidden(e -> disableAllElements(false));
            stage.setOnCloseRequest(e -> disableAllElements(false));
        }
        updateActualCell(actualSheet.
                getRow(actualRowIndex).
                getCell(actualColumnIndex).getStringCellValue());
        valueInputField.setText("");
    }


    /**
     * Method will erase given column and recalculate whole sheet.
     */
    public void deleteColumn() {

        sheetBuilder.deleteColumn(actualColumn);
        MenuItem toDelete = null;

        for (MenuItem currentColumn : currentColumns) {
            if (actualColumn.equals(currentColumn.getText())) {
                toDelete = currentColumn;
            }
        }

        MenuItem newOne;
        int index = currentColumns.indexOf(toDelete);
        if (index > 0) {
            newOne = currentColumns.get(index - 1);
        } else {
            newOne = currentColumns.get(index);
        }
        actualColumnIndex = index;
        actualColumn = newOne.getText();
        currentColumns.remove(toDelete);
        selectedColumnLabel.setText(actualColumn);
        columnSelectButton.getItems().remove(toDelete);
        actualizationColumnsSelection();
        logger.log(Level.INFO,"Column was successfully deleted.");
    }


    /**
     * Method will on ADD NEW SHEET button create initial sheet
     * with name concatenated on hardcoded pattern and new sheet
     * will be designed in accordance the predefined design.
     */
    public void addNewSheet() {
        SheetNameInitializer sheetNameInitializer = new SheetNameInitializer();

        try {
            sheetNameInitializer.setNewTrackingMonth();
            disableAllElements(true);
            sheetNameInitializer.getDialogWindow().
                    setOnCloseRequest(e -> disableAllElements(false));
            sheetNameInitializer.getDialogWindow().
                    setOnHidden(e -> disableAllElements(false));
        } catch (Exception e) {
            logger.log(Level.SEVERE,
                    "Adding sheet has invoked an exception.", e);
        }
        logger.log(Level.INFO,"New sheet has been added.");
    }

    /**
     * Method will provide actualization of the option in drop-down menu
     * for Sheet selection. Recalculation is performed on Mouse Action.
     */
    private static List<MenuItem> currentSheets = new ArrayList<>();
    private void actualizationSheetSelection() {
        if (budgetTracker.getNumberOfSheets()
                > sheetSelectButton.getItems().size()) {
            int numOfSheets = budgetTracker.getNumberOfSheets();
            currentSheets.clear();
            for (int i = 0; i < numOfSheets; i++) {
                if (!currentSheets.contains(budgetTracker.getSheetName(i))) {
                    currentSheets.add(
                            new MenuItem(budgetTracker.getSheetName(i)));
                }
            }
        }
        sheetSelectButton.getItems().clear();
        sheetSelectButton.getItems().addAll(currentSheets);
    }

    private void updateActualCell(String value) {
        displayLabel.setText(value);
    }


    /**
     * Method will provide actualization of the option in drop-down menu
     * for Row selection. Recalculation is performed on Mouse Action.
     */
    private static List<MenuItem> currentRow = new ArrayList<>();
    private void actualizationRowSelection() {
        if (actualSheet == null) {
            actualSheet = budgetTracker.getSheetAt(0);
        }
        int numberOfRows = sheetBuilder.calcSheetHeight(actualSheet);

        if (numberOfRows > rowSelectButton.getItems().size()) {
            currentRow.clear();
            for (int i = 1; i < numberOfRows; i++) {
                String cellVal =
                        actualSheet.getRow(i).getCell(0).getStringCellValue();
                if (!currentRow.contains(cellVal)) {
                    currentRow.add(
                            new MenuItem(actualSheet.
                                    getRow(i).getCell(0).getStringCellValue()));
                }
            }
        }
        rowSelectButton.getItems().clear();
        rowSelectButton.getItems().addAll(currentRow);
    }


    /**
     * Method will provide actualization of the option in drop-down menu
     * for Column selection. Recalculation is performed on Mouse Action.
     */
    private static List<MenuItem> currentColumns = new ArrayList<>();
    private void actualizationColumnsSelection() {
        if (actualSheet == null) {
            actualSheet = budgetTracker.getSheetAt(0);
        }
        int numOfColumns = actualSheet.getRow(0).getLastCellNum();
        int lastCellNum = actualSheet.getRow(0).getLastCellNum();

        if (lastCellNum > columnSelectButton.getItems().size()) {
            currentColumns.clear();
            for (int i = 0; i < numOfColumns; i++) {
                String cellVal =
                        actualSheet.getRow(0).getCell(i).getStringCellValue();
                if (!currentColumns.contains(cellVal)) {
                    currentColumns.add(
                            new MenuItem(actualSheet.
                                    getRow(0).getCell(i).getStringCellValue()));
                }
            }
        }
        columnSelectButton.getItems().clear();
        columnSelectButton.getItems().addAll(currentColumns);
    }


    /**
     * Method will update Selections Drop-Downs when changes applies.
     */
    @FXML
    private void updateOptions() {
        actualizationSheetSelection();
        actualizationColumnsSelection();
        actualizationRowSelection();
        updateColumnsLabel();
        updateRowLabel();
        updateSheetLabel();
        updateActualCell(actualSheet.
                getRow(actualRowIndex).
                getCell(actualColumnIndex).getStringCellValue());
    }

    /**
     * Method for actualization of event handlers for particular Menu Items.
     */
    @FXML
    private void updateSheetLabel() {
        for (MenuItem mi : currentSheets) {
            mi.setOnAction(e -> {
                selectedSheetLabel.setText(mi.getText());
                actualSheet = budgetTracker.getSheet(mi.getText());
                rowSelectButton.getItems().clear();
                columnSelectButton.getItems().clear();
                actualColumn = currentColumns.get(0).getText();
                actualColumnIndex = 0;
                selectedColumnLabel.setText(currentColumns.get(0).getText());
                actualRowIndex = 0;
                actualRow =
                        actualSheet.getRow(1).getCell(0).getStringCellValue();
                selectedRowLabel.setText(actualSheet.
                        getRow(1).getCell(0).getStringCellValue());
                updateActualCell(actualSheet.
                        getRow(actualRowIndex).
                        getCell(actualColumnIndex).getStringCellValue());
            });
        }
    }

    /**
     * Method for actualization of event handlers for particular Menu Items.
     */
    @FXML
    private void updateColumnsLabel() {
        for (MenuItem mi : columnSelectButton.getItems()) {
            mi.setOnAction(e -> {
                selectedColumnLabel.setText(mi.getText());
                actualColumn = mi.getText();
                int lastColumn = actualSheet.getRow(0).getLastCellNum();
                for (int i = 0; i < lastColumn; i++) {
                    if (actualColumn.
                            equals(actualSheet.getRow(0).
                                    getCell(i).getStringCellValue())) {
                        actualColumnIndex =
                                actualSheet.getRow(0).getCell(i)
                                        .getColumnIndex();
                    }
                }
                updateActualCell(actualSheet.
                        getRow(actualRowIndex).
                        getCell(actualColumnIndex).getStringCellValue());
            });
        }
    }


    /**
     * Method for adding new column. Recalculation of sheet
     * is performed and new column is inserted directly before
     * TOTAL column.
     */
    public void addNewColumn() {

        SheetNameInitializer sheetNameInitializer = new SheetNameInitializer();
        try {
            if (actualSheet != null) {
                sheetNameInitializer.addNewColumn();
                disableAllElements(true);
                sheetNameInitializer.getDialogWindow().
                        setOnCloseRequest(e -> disableAllElements(false));
                sheetNameInitializer.getDialogWindow().
                        setOnHidden(e -> disableAllElements(false));
            } else {
                actualSheet = budgetTracker.getSheetAt(0);
                sheetNameInitializer.addNewColumn();
                disableAllElements(true);
                sheetNameInitializer.getDialogWindow().
                        setOnCloseRequest(e -> disableAllElements(false));
                sheetNameInitializer.getDialogWindow().
                        setOnHidden(e -> disableAllElements(false));
            }
            updateOptions();
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Adding column has invoked an exception.",e);
        }
        logger.log(Level.INFO,"New column has been added.");
    }

    /**
     * Method for actualization of event handlers for particular Menu Items.
     */
    @FXML
    private void updateRowLabel() {
        for (MenuItem mi : currentRow) {
            mi.setOnAction(e -> {
                selectedRowLabel.setText(mi.getText());
                actualRow = mi.getText();
                for (int i = 0; i < SheetBuilder.getSheetHeight(); i++) {
                    if (actualRow.equals(actualSheet.getRow(i).
                            getCell(0).getStringCellValue())) {
                        actualRowIndex = i;
                    }
                }
                updateActualCell(actualSheet.
                        getRow(actualRowIndex).
                        getCell(actualColumnIndex).getStringCellValue());
            });
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSheetLabel.setText(SheetBuilderController.actualSheet.getSheetName());
        selectedColumnLabel.setText(SheetBuilderController.actualSheet.
                getRow(0).getCell(0).getStringCellValue());
        selectedRowLabel.setText(SheetBuilderController.actualSheet.
                getRow(1).getCell(0).getStringCellValue());
        displayLabel.setText(SheetBuilderController.actualSheet.
                getRow(actualRowIndex).
                getCell(actualColumnIndex).getStringCellValue());
    }
}
