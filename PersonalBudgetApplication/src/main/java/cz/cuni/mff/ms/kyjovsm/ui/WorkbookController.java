package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
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

    public Stage getElement(){
        return this.window;
    }

    void createWorkbook() throws IOException{
        window = new Stage();
        workBookInitializer = new Scene(loadWorkbookFXML());
        window.setScene(workBookInitializer);
        window.show();
    }

    public void setUpNameOfDocument() throws FXMLLoaderException{
        String nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
                inputField.setText("");
            } else {
                SheetBuilder.setNameOfTheDocument(nameOfDoc);
                workBookInitializer = new Scene(loadInitialMonthDialogFXML());
                stage.setScene(workBookInitializer);
                stage.show();
            }
        }
        catch (Exception e){
            throw new FXMLLoaderException();
        }
    }

    @FXML
    private void setupInitialMonth() throws FXMLLoaderException{
        Stage stage = null;
        alertBox = new AlertBox();
        try {
            stage = (Stage) submitMonthButton.getScene().getWindow();
        }catch (Exception e){
            throw new FXMLLoaderException();
        }

        String monthInput = inputField.getCharacters().toString();
        try {
            if (!monthInput.isEmpty() && monthInput.matches("[0-9]*")) {

                int month = Integer.parseInt(monthInput);

                if(month >= 1 && month <=12) {
                    WorkbookBuilder workbookBuilder = new WorkbookBuilder();
                    WorkbookBuilder.setInitialMonth(month);
                    App.changeScene(new Scene(loadSheetFXML()));
                    SheetBuilderController.setBudget_tracker(workbookBuilder.createInitialWorkbook());
                    stage.close();
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

    private Parent loadWorkbookFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorkbookController.class.getResource( "Workbook.fxml"));
        return fxmlLoader.load();
    }

    private Parent loadSheetFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetBuilderController.class.getResource("Sheet.fxml"));
        return fxmlLoader.load();
    }

    private Parent loadInitialMonthDialogFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorkbookController.class.getResource( "InitialMonthDialog.fxml"));
        return fxmlLoader.load();
    }

}

