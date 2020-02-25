package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AlertBox {
    private Scene alertBoxScene;

    @FXML
    Label errorMessage;

    @FXML
    Button closeButton;

    public  void displayAlertBox(String errorStatus){
        Stage alertBoxStage = new Stage();
        try {
            if (errorStatus.equals("AlertBoxEmptyInput")) {
                alertBoxScene = new Scene(loadFXML("AlertBoxEmptyInput"));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        alertBoxStage.setScene(alertBoxScene);
        alertBoxStage.show();
    }

    public void closeAlertBox(){
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AlertBox.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
