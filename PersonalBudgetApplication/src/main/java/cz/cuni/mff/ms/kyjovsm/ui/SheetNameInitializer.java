package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;

import cz.cuni.mff.ms.kyjovsm.workbook.SheetBuilder;
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
    private Scene dialogScene;
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

    public Scene getDialogScene(){
        return dialogScene;
    }

    public void setNewTruckingMonth() throws FXMLLoaderException{
        dialogWindow.setResizable(false);
        try {
             dialogWindow.setScene(new Scene((loadSheetNameInitializerFXML())));
        }catch (IOException ioe){
            throw new FXMLLoaderException();
        }
        dialogWindow.show();
    }

    @FXML
    private void submitMonth(){
        String newInitialMonth = inputLine.getCharacters().toString();
        Stage stage = (Stage) submitButton.getScene().getWindow();
        if (newInitialMonth.isEmpty() || newInitialMonth.isBlank()){
            AlertBox alertBox = new AlertBox();
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_EMPTY_INPUT);
        } else if (!newInitialMonth.matches("[0-9]*")){
            AlertBox alertBox = new AlertBox();
            alertBox.displayAlertBox(AlertBox.ALERT_BOX_INVALID_INPUT);
        }else {
            SheetBuilder sheetBuilder = new SheetBuilder();
            sheetBuilder.createNewSheet(newInitialMonth);;
            stage.close();
        }
    }

    private Parent loadSheetNameInitializerFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetNameInitializer.class.getResource( "SheetNameInitializer.fxml"));
        return fxmlLoader.load();
    }
}
