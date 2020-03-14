package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkbookController {

    @FXML
    Button submitMonthButton;
    @FXML
    Button submitButton;
    @FXML
    TextField inputField;
    private Stage window;
    private AlertBox alertBox;
    private Scene workBookInitializer;
    private String relatedFxmlWorkbook = "Workbook.fxml";
    private String relatedFxmlSheet = "Sheet.fxml";
    private String relatedFxmlInitialMonthDialog = "InitialMonthDialog.fxml";
    private String workbookControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.WorkbookController";
    private String sheetBuilderControllerClassName = "cz.cuni.mff.ms.kyjovsm.ui.SheetBuilderController";
    private Tools tool;

    public static void setNameOfDoc(String nameOfDoc) {
        WorkbookController.nameOfDoc = nameOfDoc;
    }

    private static String nameOfDoc;

    public static String getNameOfDoc() {
        return nameOfDoc;
    }

    public WorkbookController(){
        tool = new Tools();
    }

    public Stage getElement(){
        return this.window;
    }

    void createWorkbook() throws FXMLLoaderException{
        try {
            window = new Stage();
            workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName), relatedFxmlWorkbook));
            window.setScene(workBookInitializer);
            window.show();
        }catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlWorkbook);
        }
    }

    public void setUpNameOfDocument() throws FXMLLoaderException{
        nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
                inputField.setText("");
            } else {
                SheetBuilder.setNameOfTheDocument(nameOfDoc);
                workBookInitializer = new Scene(tool.loadFXML(Class.forName(workbookControllerClassName),relatedFxmlInitialMonthDialog));
                stage.setScene(workBookInitializer);
                stage.show();
            }
        }
        catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlInitialMonthDialog);
        }
    }

    @FXML
    private void setupInitialMonth(){
        Stage stage = null;
        alertBox = new AlertBox();
        try {
            stage = (Stage) submitMonthButton.getScene().getWindow();
        }catch (Exception e){
            e.printStackTrace();
        }

        String monthInput = inputField.getCharacters().toString();
        try {
            if (!monthInput.isEmpty() && monthInput.matches("[0-9]*")) {

                int month = Integer.parseInt(monthInput);

                if(month >= 1 && month <=12) {
                    try {

                        WorkbookBuilder workbookBuilder = new WorkbookBuilder();
                        WorkbookBuilder.setInitialMonth(month);
                        App.changeScene(new Scene(tool.loadFXML(Class.forName(sheetBuilderControllerClassName),relatedFxmlSheet)));
                        SheetBuilderController.setBudget_tracker(workbookBuilder.createInitialWorkbook());
                        stage.close();
                    }catch (Exception e){
                        throw new FXMLLoaderException(relatedFxmlSheet);
                    }
                }
                else{
                    alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
                    inputField.setText("");
                }
            }
            else{
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
                inputField.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

