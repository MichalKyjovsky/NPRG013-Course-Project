package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;

import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SheetNameInitializer {

    @FXML
    private Button submitButton;
    @FXML
    private TextField inputLine;
    private Stage dialogWindow;
    private String sheetNameInitializerClassName = "cz.cuni.mff.ms.kyjovsm.additionalUtils.SheetNameInitializer";
    private String relatedFxmlSheet = "SheetNameInitializer.fxml";
    private String getRelatedFxmlColumn = "ColumnNameInitializer.fxml";
    private AlertBox alertBox;
    private SheetBuilder sheetBuilder;
    private Stage stage;

    public SheetNameInitializer(){
        sheetBuilder = new SheetBuilder();
        alertBox = new AlertBox();
        dialogWindow = new Stage();
    }

    public Stage getDialogWindow() {
        return dialogWindow;
    }

    Button getSubmitButton(){
        return this.submitButton;
    }


    /**
     * Based on user input method will initiate new  tracking month for given input and
     * new sheet based on the given information will be created.
     * @throws FXMLLoaderException
     */
    public void setNewTruckingMonth() throws FXMLLoaderException{
        Tools tool = new Tools();
        dialogWindow.setResizable(false);
        try {
             dialogWindow.setScene(new Scene(tool.loadFXML(Class.forName(sheetNameInitializerClassName), relatedFxmlSheet)));
        }catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlSheet);
        }
        dialogWindow.show();
    }


    /**
     * When user press "ADD NEW COLUMN" method starts initiation process.
     * @throws FXMLLoaderException
     */
    public void addNewColumn() throws FXMLLoaderException{
        dialogWindow.setResizable(false);
        Tools tool = new Tools();
        try{
            dialogWindow.setScene(new Scene(tool.loadFXML(Class.forName(sheetNameInitializerClassName), getRelatedFxmlColumn)));
        }catch(Exception e){
            throw new FXMLLoaderException(getRelatedFxmlColumn);
        }
        dialogWindow.show();
    }

    /**
     * When a new tracking month is provided by user, method
     * wul then secure new sheet initiation and creation.
     */
    @FXML
    private void submitMonth(){
        Stage stage = (Stage) submitButton.getScene().getWindow();
        String newInitialMonth = inputLine.getCharacters().toString();
        AlertBox alertBox = new AlertBox();
        SheetBuilder sheetBuilder = new SheetBuilder();

        if (newInitialMonth.isEmpty() || newInitialMonth.isBlank()){
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
        } else if (!newInitialMonth.matches("[0-9]*")){
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
        }else {
            sheetBuilder.createNewSheet(newInitialMonth);
            stage.close();
        }
    }

    /**
     * When name of new column is provided by user, new column is then
     * initiated and refactoring is performed.
     */
    @FXML
    private void submitName() {
        stage = (Stage) submitButton.getScene().getWindow();
        String newColumnName = inputLine.getCharacters().toString();

        if(newColumnName.isBlank() || newColumnName.isEmpty()){
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
        }
        else if (!newColumnName.matches("[a-zA-z][a-zA-Z0-9_-]*")){
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
        }
        else{
            sheetBuilder.createNewColumn(newColumnName);
            stage.close();
        }
    }
}
