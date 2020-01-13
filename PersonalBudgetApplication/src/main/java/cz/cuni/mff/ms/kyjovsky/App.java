package cz.cuni.mff.ms.kyjovsky;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class App extends Application {

    public static Stage stage;
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 300;

    @Override
    public void start(Stage window) throws IOException {
        stage = window;
        stage.setTitle("Personal budget application");


        FileChooser fileChooser = new FileChooser();
        CoverPage coverPage = new CoverPage();

        Button button = new Button();
        button.setText("Open file from local storage");
        button.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
        });

        Button button1 = new Button();
        VBox vBox = new VBox(20);
        vBox.getChildren().addAll(button,button1);
        Scene scene = new Scene(vBox, APPLICATION_WIDTH,APPLICATION_HEIGHT);
        button1.setText("Open file from OneDrive");
        button1.setOnAction(e -> stage.setScene(coverPage.displayCoverPage(scene)));


        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}