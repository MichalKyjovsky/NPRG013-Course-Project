package cz.cuni.mff.ms.kyjovsm.workbook;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class workbookBuilder {

    @FXML
    Button submitButton;
    @FXML
    TextField inputField;

    public void createWorkbook() throws IOException{
        Stage window = new Stage();
        Scene scene = new Scene(loadFXML("workbookBuilder"));
        window.setScene(scene);
        window.show();
    }

    public void setUpNameOfDocument(){
        String nameOfDoc = inputField.getText();

    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(workbookBuilder.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
