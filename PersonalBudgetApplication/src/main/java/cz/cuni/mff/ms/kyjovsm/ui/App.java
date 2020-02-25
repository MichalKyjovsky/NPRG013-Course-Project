package cz.cuni.mff.ms.kyjovsm.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        Scene scene = new Scene(loadFXML("LandingPage"));
        window.setScene(scene);
        window.show();
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void changeScene(Scene scene){
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) {
        launch();
    }

}