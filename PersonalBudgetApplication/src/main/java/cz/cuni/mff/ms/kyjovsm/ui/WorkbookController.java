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
import org.apache.poi.ss.usermodel.Workbook;


public class WorkbookController {

    @FXML
    private Button submitButton;
    @FXML
<<<<<<< HEAD
    TextField inputField;
    private Stage window;
=======
    private TextField inputField;
    private Stage window;
    private static final String XLSX_SUFFIX = ".xlsx";
>>>>>>> master
    private String nameOfDoc;
    private static final String XLSX_SUFFIX = ".xslx";

    public Stage getElement(){
        return this.window;
    }

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

            Stage stage = (Stage) submitButton.getScene().getWindow();

            if (!nameOfDoc.matches("[a-zA-Z0-9][a-zA-Z0-9_ -]*")) {
                alertBox.displayAlertBox("AlertBoxEmptyInput");
                inputField.setText("");
<<<<<<< HEAD
            } else {
=======
            }
            else {
                nameOfDoc += XLSX_SUFFIX;
>>>>>>> master
                System.out.println(nameOfDoc);
                stage = (Stage) submitButton.getScene().getWindow();
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


    String getNameOfDoc() {
        return nameOfDoc;
    }
}

