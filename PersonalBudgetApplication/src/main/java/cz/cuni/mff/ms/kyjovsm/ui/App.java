package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage window;
    private Scene  scene;
    private String relatedFxmlLandingPage = "LandingPage.fxml";
    private static final String logo = "cz/cuni/mff/ms/kyjovsm/pics_source/vault.png";
    private static final String appClassName = "cz.cuni.mff.ms.kyjovsm.ui.App";
    private static final int windowHeight = 660;
    private static final int windowWith = 950;


    @Override
    public void start(Stage stage) throws FXMLLoaderException {
        Tools tools = new Tools();
        window = stage;
        window.setOnCloseRequest(e -> System.exit(0));
        window.getIcons().add(new Image(logo));
        try {
            scene = new Scene(tools.loadFXML(Class.forName(appClassName),relatedFxmlLandingPage));
        }catch (Exception e){
            throw new FXMLLoaderException(relatedFxmlLandingPage);
        }

        setWindowSize(windowHeight,windowHeight);
        window.setScene(scene);
        window.show();
    }


    public static void changeScene(Scene scene){
        window.setScene(scene);
        window.show();
    }

    private void setWindowSize(double height, double width){
        window.setMinHeight(height);
        window.setMinWidth(width);
    }

    public static void main(String[] args) {
        launch();
    }

}