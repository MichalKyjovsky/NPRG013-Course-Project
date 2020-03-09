package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.nio.file.Path;
import java.util.ArrayList;

public class SheetBuilderController {
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

    public void updateOptions() {
        actualizationSheetSelection();
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
