package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBoxSaveAndLeave;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FileFormatException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SheetBuilderController {
    @FXML
    SplitMenuButton rowSelectButton;
    @FXML
    SplitMenuButton columnSelectButton;
    @FXML
    SplitMenuButton sheetSelectButton;
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

    static void setBudget_tracker(Workbook budget_tracker) {
        SheetBuilderController.budget_tracker = budget_tracker;
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
            fileChooser.setInitialDirectory(Path.of(System.getProperty("user.home")).toFile());
            fileChooser.showSaveDialog(new Stage());
        }
        else {
            throw new FileFormatException();
        }
    }

    public void sendValueToCell() {
        sheetBuilder.setCellValue(valueInputField.getCharacters().toString());
    }

    public void deleteColumn(ActionEvent actionEvent) {
        System.out.println(SheetBuilder.getNameOfTheDocument());
        System.out.println(WorkbookBuilder.getInitialMonth());
    }
}
