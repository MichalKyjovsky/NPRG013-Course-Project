package cz.cuni.mff.ms.kyjovsm.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage window;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        window.setOnCloseRequest(e -> System.exit(0));
        window.getIcons().add(new Image("cz/cuni/mff/ms/kyjovsm/pics_source/vault.png"));
        Scene scene = new Scene(loadLandingPageFXML());
        window.setScene(scene);
        window.show();
    }

    private Parent loadLandingPageFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(  "LandingPage.fxml"));
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