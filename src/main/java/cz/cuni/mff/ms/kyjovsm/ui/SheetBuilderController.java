package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SheetBuilderController {
    /**
     * Instance of class Label displaying actual selected row.
     */
    @FXML
    private Label selectedRowLabel;
    /**
     * Instance of class Label displaying actual selected column.
     */
    @FXML
    private Label selectedColumnLabel;
    /**
     * Instance of class Label displaying actual selected sheet.
     */
    @FXML
    private Label selectedSheetLabel;
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
    /**
     * Instance of class Tools enabling functionality
     * of changing between current displayed Scene.
     */
    private final Tools tool = new Tools();
    /**
     * Instance of class Logger enabling easier tracking
     * and debugging, which is documented in generated log file.
     */
    private final Logger logger = Logger.getLogger("SheetBuilderController");

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

        if (!SheetBuilder.getNameOfTheDocument().strip().isEmpty()) {
            fileChooser.setInitialFileName(SheetBuilder.getNameOfTheDocument());
            fileChooser.setInitialDirectory(Path.
                    of(SheetBuilder.getNameOfTheDocument()).
                    getParent().toFile());
            File file = fileChooser.showSaveDialog(new Stage());
        } else {
            throw new FileFormatException();
        }
        logger.fine("Workbook was successfully saved.");
    }


    /**
     * Method sends given input if correct to the particular cell
     * whenever is SUBMIT button pressed.
     */
    public void sendValueToCell() {
        if (valueInputField.getCharacters().toString().matches("[0-9]+")) {
            sheetBuilder.
                    setCellValue(valueInputField.getCharacters().toString(),
                    actualSheet, actualColumnIndex, actualRowIndex);
            logger.fine(
                    String.format("Value in column: %s for a day:"
                            + " %s was successfully updated.",
                            actualColumn, actualRow));
        } else {
            AlertBox alertBox = new AlertBox();
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
            disableAllElements(true);
            Stage stage = alertBox.getAlertBoxStage();
            stage.setOnHidden(e -> disableAllElements(false));
            stage.setOnCloseRequest(e -> disableAllElements(false));
        }
        valueInputField.setText("");
    }


    /**
     * Method will erase given column and recalculate whole sheet.
     */
    public void deleteColumn() {
        sheetBuilder.deleteColumn(actualColumn);
        MenuItem toDelete = null;

        for (int i = 0; i < currentColumns.size(); i++) {
            if (actualColumn.equals(currentColumns.get(i).getText())) {
                toDelete = currentColumns.get(i);
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
        logger.fine("Column was successfully deleted.");
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
            logger.fine("New sheet has been added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            logger.fine("New column has been added.");
            updateOptions();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            });
        }
    }
}
