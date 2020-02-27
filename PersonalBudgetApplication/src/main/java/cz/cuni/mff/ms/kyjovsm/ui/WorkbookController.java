package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import cz.cuni.mff.ms.kyjovsm.workbook.WorkbookBuilder;


public class WorkbookController {

    @FXML
    Button submitButton;
    @FXML
    TextField inputField;
    private Stage window;
    private String nameOfDoc;
    private static final String XLSX_SUFFIX = ".xslx";

    public Stage getElement(){
        return this.window;
    }

    public void createWorkbook() throws IOException{
        window = new Stage();
        Scene scene = new Scene(loadFXMLforWorkbook("Workbook"));
        window.setScene(scene);
        window.show();
    }

    public void setUpNameOfDocument(){
        nameOfDoc = inputField.getText();
        AlertBox alertBox = new AlertBox();
        try {
            Stage stage = (Stage) submitButton.getScene().getWindow();;
            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox("AlertBoxEmptyInput");
                inputField.setText("");
            } else {
                System.out.println(nameOfDoc);
                stage.close();
                App.changeScene(new Scene(loadFXMLforSheet("Sheet")));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            alertBox.displayAlertBox("AlertBoxEmptyInput");
        }
    }

    private Parent loadFXMLforWorkbook(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorkbookController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private Parent loadFXMLforSheet(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SheetBuilderController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public String getNameOfDoc() {
        return nameOfDoc;
    }

}

