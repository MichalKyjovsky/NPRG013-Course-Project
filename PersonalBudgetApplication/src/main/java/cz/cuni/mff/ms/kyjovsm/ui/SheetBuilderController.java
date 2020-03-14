package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class SheetBuilderController {
    @FXML
    Label selectedRowLabel;
    @FXML
    Label selectedColumnLabel;
    @FXML
    Label selectedSheetLabel;
    @FXML
    MenuButton rowSelectButton;
    @FXML
    MenuButton columnSelectButton;
    @FXML
    MenuButton sheetSelectButton;
    @FXML
    Button submitValueButton;
    @FXML
    Button saveButton;
    @FXML
    Button addColumnButton;
    @FXML
    Button addSheetButton;
    @FXML
    Button homeButton;
    @FXML
    Button deleteColumnButton;
    @FXML
    TextField valueInputField;
    private SheetBuilder sheetBuilder = new SheetBuilder();
    private static Workbook budget_tracker;
    private static final String USER_HOME_DIR = "user.home";

    private static Sheet actualSheet;
    private static String actualColumn;
    private static String actualRow;
    private static int actualColumnIndex;
    private static int actualRowIndex;

    public static Sheet getActualSheet(){
        return SheetBuilderController.actualSheet;
    }

    public static void setBudget_tracker(Workbook budget_tracker) {
        SheetBuilderController.budget_tracker = budget_tracker;
    }

    public static Workbook getBudget_tracker() {
        return budget_tracker;
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
     * @param statement
     */
    private void disableAllElements(boolean statement){
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
    public void saveDocument() throws FileFormatException{
        FileChooser fileChooser = new FileChooser();

        if (!SheetBuilder.getNameOfTheDocument().strip().isEmpty()) {
            fileChooser.setInitialFileName(SheetBuilder.getNameOfTheDocument());
            fileChooser.setInitialDirectory(Path.of(SheetBuilder.getNameOfTheDocument()).getParent().toFile());
            File file = fileChooser.showSaveDialog(new Stage());
            System.out.println(file);
        }
        else {
            throw new FileFormatException();
        }
    }


    /**
     * Method sends given input if correct to the particular cell
     * whenever is SUBMIT button pressed.
     */
    public void sendValueToCell() {
        if(valueInputField.getCharacters().toString().matches("[0-9]+")) {
            sheetBuilder.setCellValue(valueInputField.getCharacters().toString(),actualSheet,actualColumnIndex,actualRowIndex);
        }
        else{
            AlertBox alertBox = new AlertBox();
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
            disableAllElements(true);
            Button button = alertBox.getCloseButton();
            Stage stage = alertBox.getAlertBoxStage();
            stage.setOnHidden(e -> {disableAllElements(false);});
            stage.setOnCloseRequest(e -> {disableAllElements(false);});
        }
        valueInputField.setText("");
    }


    /**
     * Method will erase given column and recalculate whole sheet.
     */
    public void deleteColumn() {
        sheetBuilder.deleteColumn(actualColumn);
        updateOptions();
    }


    /**
     * Method will on ADD NEW SHEET button create initial sheet
     * with name concatenated on hardcoded pattern and new sheet
     * will be designed in accordance the predefined design.
     */
    public void addNewSheet(){
        SheetNameInitializer sheetNameInitializer = new SheetNameInitializer();
        try {
            sheetNameInitializer.setNewTruckingMonth();
            disableAllElements(true);
            sheetNameInitializer.getDialogWindow().setOnCloseRequest(e -> disableAllElements(false));
            sheetNameInitializer.getDialogWindow().setOnHidden(e -> disableAllElements(false));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    ArrayList<MenuItem> currentSheets = new ArrayList<>();

    /**
     * Method will provide actualization of the option in drop-down menu
     * for Sheet selection. Recalculation is performed on Mouse Action.
     */
    private void actualizationSheetSelection() {
        if (budget_tracker.getNumberOfSheets() > sheetSelectButton.getItems().size()) {
            int numOfSheets = budget_tracker.getNumberOfSheets();
            currentSheets.clear();
            for (int i = 0; i < numOfSheets; i++) {
                if (!currentSheets.contains(budget_tracker.getSheetName(i))) {
                    currentSheets.add(new MenuItem(budget_tracker.getSheetName(i)));
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
    ArrayList<MenuItem> currentRow = new ArrayList<>();
    private void actualizationRowSelection(){
        if(actualSheet == null){
            actualSheet = budget_tracker.getSheetAt(0);
        }
        int numberOfRows = sheetBuilder.calcSheetHeight(actualSheet);

        if(numberOfRows > rowSelectButton.getItems().size()){
            currentRow.clear();
            for (int i = 1; i < numberOfRows; i++){
                if(!currentRow.contains(actualSheet.getRow(i).getCell(0).getStringCellValue())){
                    currentRow.add(new MenuItem(actualSheet.getRow(i).getCell(0).getStringCellValue()));
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
    ArrayList<MenuItem> currentColumns = new ArrayList<>();
    private void actualizationColumnsSelection(){
        if(actualSheet == null){
            actualSheet = budget_tracker.getSheetAt(0);
        }
        int numOfColumns = actualSheet.getRow(0).getLastCellNum();

        if(actualSheet.getRow(0).getLastCellNum() > columnSelectButton.getItems().size()){
            currentColumns.clear();
            //TODO:We do not want to include columns with total amount and Date, those should be immutable
            for(int i = 0; i < numOfColumns; i++){
                if(!currentColumns.contains(actualSheet.getRow(0).getCell(i).getStringCellValue())){
                    currentColumns.add(new MenuItem(actualSheet.getRow(0).getCell(i).getStringCellValue()));
                }
            }
        }
        columnSelectButton.getItems().clear();
        columnSelectButton.getItems().addAll(currentColumns);
    }


    /**
     * Method will update Selections Drop-Downs when changes applies.
     */
    private void updateOptions() {
        actualizationSheetSelection();
        actualizationColumnsSelection();
        actualizationRowSelection();
    }


    /**
     * Method for actualization of event handlers for particular Menu Items
     */
    @FXML
    private void updateSheetLabel() {
        for(MenuItem mi : currentSheets){
            mi.setOnAction(e -> {
                selectedSheetLabel.setText(mi.getText());
                actualSheet = budget_tracker.getSheet(mi.getText());
                System.out.println(budget_tracker.getSheet(mi.getText()));
            });
        }
    }

    /**
     * Method for actualization of event handlers for particular Menu Items
     */
    @FXML
    private void updateColumnsLabel() {
        for (MenuItem mi : currentColumns){
            mi.setOnAction(e ->{
                System.out.println(mi.getText());
                selectedColumnLabel.setText(mi.getText());
                actualColumn = mi.getText();
                for(int i = 0; i < actualSheet.getRow(0).getLastCellNum(); i++) {
                    if (actualColumn.equals(actualSheet.getRow(0).getCell(i).getStringCellValue())){
                        actualColumnIndex = actualSheet.getRow(0).getCell(i).getColumnIndex();
                    }
                }
            });
        }
    }


    /**
     * Method for adding new column. Recalculation of sheet is performed and new column is inserted directly before
     * TOTAL column.
     */
    public void addNewColumn() {
        SheetNameInitializer sheetNameInitializer = new SheetNameInitializer();
        try {
            if (actualSheet != null) {
                sheetNameInitializer.addNewColumn();
                disableAllElements(true);
                sheetNameInitializer.getDialogWindow().setOnCloseRequest(e -> disableAllElements(false));
                sheetNameInitializer.getDialogWindow().setOnHidden(e -> disableAllElements(false));
            } else {
                actualSheet = budget_tracker.getSheetAt(0);
                sheetNameInitializer.addNewColumn();
                disableAllElements(true);
                sheetNameInitializer.getDialogWindow().setOnCloseRequest(e -> disableAllElements(false));
                sheetNameInitializer.getDialogWindow().setOnHidden(e -> disableAllElements(false));
            }
            updateOptions();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method for actualization of event handlers for particular Menu Items
     */
    @FXML
    private void updateRowLabel() {
        for (MenuItem mi : currentRow){
            mi.setOnAction(e ->{
                System.out.println(mi.getText());
                selectedRowLabel.setText(mi.getText());
                actualRow = mi.getText();
                for(int i = 0; i < SheetBuilder.getSheetHeight(); i++) {
                    if (actualRow.equals(actualSheet.getRow(i).getCell(0).getStringCellValue())){
                        actualRowIndex = i;
                    }
                }
            });
        }
    }
}
