package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WorkbookController {
    @FXML
    private MenuButton selectMonthButton;
    @FXML
    private Button submitMonthButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField inputField;
    private Stage window;
    private AlertBox alertBox;
    private Scene workBookInitializer;
    private final String workbookControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.WorkbookController";
    private final Tools tool;

    public static void setNameOfDoc(String nameOfDoc) {
        WorkbookController.nameOfDoc = nameOfDoc;
    }

    private static String nameOfDoc;

    public WorkbookController() {
        tool = new Tools();
    }

    public Stage getElement() {
        return this.window;
    }


    /**
     * Method enables user to invoke setup dialog of new Workbook.
     * @throws FXMLLoaderException
     */
    void createWorkbook() throws FXMLLoaderException {
        String relatedFxmlWorkbook = "ui/Workbook.fxml";
        try {
            window = new Stage();
            workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName), relatedFxmlWorkbook));
            window.setScene(workBookInitializer);
            window.show();
        } catch (Exception e) {
            throw new FXMLLoaderException(relatedFxmlWorkbook);
        }
    }


    /**
     * Method enables user to setup new Workbook name.
     * @throws FXMLLoaderException
     */
    public void setUpNameOfDocument() throws FXMLLoaderException {
        nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        String relatedFxmlInitialMonthDialog = "ui/InitialMonthDialog.fxml";
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
                inputField.setText("");
            } else {
                SheetBuilder.setNameOfTheDocument(nameOfDoc);
                workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName), relatedFxmlInitialMonthDialog));
                stage.setScene(workBookInitializer);
                stage.show();
            }
        } catch (Exception e) {
            throw new FXMLLoaderException(relatedFxmlInitialMonthDialog);
        }
    }


    /**
     * Method enables to user set up initial tracking month.
     */
    @FXML
    private void setupInitialMonth() {
        List<MenuItem> menuItems = selectMonthButton.getItems();
        String relatedFxmlSheet = "ui/Sheet.fxml";
        String sheetBuilderControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController";

        for (MenuItem mi : menuItems){
            mi.setOnAction(e -> {
                int index = menuItems.indexOf(mi) + 1;
                WorkbookBuilder.setInitialMonth(index);
                try {
                    Stage stage = (Stage) selectMonthButton.getScene().getWindow();
                    App.changeScene(new Scene(tool.loadFXML(Class.forName(sheetBuilderControllerClassName), relatedFxmlSheet)));
                    SheetBuilderController.setBudgetTracker(new WorkbookBuilder().createInitialWorkbook());
                    stage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });
        }

    }
}

