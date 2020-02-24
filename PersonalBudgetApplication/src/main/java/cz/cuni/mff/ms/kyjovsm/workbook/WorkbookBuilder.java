package cz.cuni.mff.ms.kyjovsm.workbook;

import cz.cuni.mff.ms.kyjovsm.ui.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkbookBuilder {

    @FXML
    Button submitButton;
    @FXML
    TextField inputField;

    AlertBox alertBox;

    public void createWorkbook() throws IOException{
        Stage window = new Stage();
        Scene scene = new Scene(loadFXML("WorkbookBuilder"));
        window.setScene(scene);
        window.show();
    }

    public void setUpNameOfDocument(){
        String nameOfDoc = inputField.getText();
        alertBox = new AlertBox();
        try {
            if(!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_-]*")) {
                alertBox.displayAlertBox("AlertBoxEmptyInput");
                inputField.setText("");
            }
            else {
                System.out.println(nameOfDoc);
                Stage stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            alertBox.displayAlertBox("AlertBoxEmptyInput");
        }

    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorkbookBuilder.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
