package cz.cuni.mff.ms.kyjovsky;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class ModePage {

    private static final String ONE_DRIVE_DIALOG_BTN = "OneDrive";
    private static final String LOCAL_STORAGE_DIALOG_BTN = "Open from local device";
    private static final String CREATE_NEW_SHEET_BTN = "Create new sheet";
    private static File selectedFile;

    public static Scene displayModePage(){
        VBox layout = new VBox();
        ComponentBuilder componentBuilder = new ComponentBuilder();

        layout.getChildren().add(setUpLocalStorageBtn());
        layout.setAlignment(Pos.CENTER);

        return new Scene(layout,App.APPLICATION_WIDTH,App.APPLICATION_HEIGHT);
    }

    private static Button setUpLocalStorageBtn(){
        FileChooser fileChooser = new FileChooser();

        Button localStorageBtn = new Button();
        localStorageBtn.setText(LOCAL_STORAGE_DIALOG_BTN);
        localStorageBtn.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(App.stage);
        });
        return  localStorageBtn;
    }
}
