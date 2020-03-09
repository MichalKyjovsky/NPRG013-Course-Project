package cz.cuni.mff.ms.kyjovsm.ui;

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
import java.nio.file.Path;
import java.util.ArrayList;

public class SheetBuilderController {
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
    private static int actualColumnIndex;

    public static Sheet getActualSheet(){
        return SheetBuilderController.actualSheet;
    }

    static void setBudget_tracker(Workbook budget_tracker) {
        SheetBuilderController.budget_tracker = budget_tracker;
    }

    public static Workbook getBudget_tracker() {
        return budget_tracker;
    }


    public void goToHomePage() {
        AlertBoxSaveAndLeave alertBoxSaveAndLeave = new AlertBoxSaveAndLeave();
        disableAllElements(true);
        alertBoxSaveAndLeave.invokeDialog();
        Stage frontStage = alertBoxSaveAndLeave.getAlertBox();

        frontStage.setOnCloseRequest(e -> disableAllElements(false));
        frontStage.setOnHidden(e -> disableAllElements(false));
    }

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

    public void saveDocument() throws FileFormatException{
        FileChooser fileChooser = new FileChooser();

        if (!SheetBuilder.getNameOfTheDocument().strip().isEmpty()) {
            fileChooser.setInitialFileName(SheetBuilder.getNameOfTheDocument());
            fileChooser.setInitialDirectory(Path.of(System.getProperty(USER_HOME_DIR)).toFile());
            fileChooser.showSaveDialog(new Stage());
        }
        else {
            throw new FileFormatException();
        }
    }

    public void sendValueToCell() {
        sheetBuilder.setCellValue(valueInputField.getCharacters().toString());
    }

    public void deleteColumn() {
        System.out.println(SheetBuilder.getNameOfTheDocument());
        System.out.println(WorkbookBuilder.getInitialMonth());
    }

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

    ArrayList<MenuItem> currentColumns = new ArrayList<>();
    private void actualizationColumnsSelection(){
        if(actualSheet == null){
            actualSheet = budget_tracker.getSheetAt(0);
        }
        int numOfColumns = actualSheet.getRow(0).getLastCellNum();

        if(actualSheet.getRow(0).getLastCellNum() > columnSelectButton.getItems().size()){
            currentColumns.clear();
            //We do not want to include columns with total amount and Date, those should be immutable
            for(int i = 0; i < numOfColumns; i++){
                if(!currentColumns.contains(actualSheet.getRow(0).getCell(i).getStringCellValue())){
                    currentColumns.add(new MenuItem(actualSheet.getRow(0).getCell(i).getStringCellValue()));
                }
            }
        }
        columnSelectButton.getItems().clear();
        columnSelectButton.getItems().addAll(currentColumns);
    }

    public void updateOptions() {
        actualizationSheetSelection();
        actualizationColumnsSelection();
    }


    public void updateSheetLabel() {
        for(MenuItem mi : currentSheets){
            mi.setOnAction(e -> {
                selectedSheetLabel.setText(mi.getText());
                actualSheet = budget_tracker.getSheet(mi.getText());
                System.out.println(budget_tracker.getSheet(mi.getText()));
            });
        }
    }

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

    public void addNewColumn() {
        SheetNameInitializer sheetNameInitializer = new SheetNameInitializer();
        if (actualSheet != null) {
            sheetNameInitializer.addNewColumn();
            disableAllElements(true);
            sheetNameInitializer.getDialogWindow().setOnCloseRequest(e -> disableAllElements(false));
            sheetNameInitializer.getDialogWindow().setOnHidden(e -> disableAllElements(false));
        }
        else {
            actualSheet = budget_tracker.getSheetAt(0);
            sheetNameInitializer.addNewColumn();
            disableAllElements(true);
            sheetNameInitializer.getDialogWindow().setOnCloseRequest(e -> disableAllElements(false));
            sheetNameInitializer.getDialogWindow().setOnHidden(e -> disableAllElements(false));
        }
    }
}
