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

    public SheetNameInitializer(){
        dialogWindow = new Stage();
    }

    public Stage getDialogWindow() {
        return dialogWindow;
    }

    Button getSubmitButton(){
        return this.submitButton;
    }

    public void setNewTruckingMonth() throws FXMLLoaderException{
        dialogWindow.setResizable(false);
        try {
             dialogWindow.setScene(new Scene(loadSheetNameInitializerFXML()));
        }catch (IOException ioe){
            throw new FXMLLoaderException();
        }
        dialogWindow.show();
    }

    public void addNewColumn(){
        dialogWindow.setResizable(false);
        try{
            dialogWindow.setScene(new Scene(loadColumnNameInitializerFXML()));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
        dialogWindow.show();
    }

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
            sheetBuilder.createNewSheet(newInitialMonth);;
            stage.close();
        }
    }

    @FXML
    private void submitName() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        String newColumnName = inputLine.getCharacters().toString();
        AlertBox alertBox = new AlertBox();
        SheetBuilder sheetBuilder = new SheetBuilder();

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


    private Parent loadSheetNameInitializerFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetNameInitializer.class.getResource( "SheetNameInitializer.fxml"));
        return fxmlLoader.load();
    }

    private Parent loadColumnNameInitializerFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetNameInitializer.class.getResource( "ColumnNameInitializer.fxml"));
        return fxmlLoader.load();
    }
}
