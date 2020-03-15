package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.additionalUtils.Tools;
import cz.cuni.mff.ms.kyjovsm.applicationExceptions.FXMLLoaderException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage window;
    private Scene  scene;
    private static final String LOGO = "pics_source/vault.png";
    private static final String APP_CLASS_NAME = "cz.cuni.mff.ms.kyjovsm.ui.App";
    private static final int WINDOW_HEIGHT = 660;
    private static final int WINDOW_WIDTH = 660;


    @Override
    public void start(final Stage stage) throws FXMLLoaderException {
        Tools tools = new Tools();
        window = stage;
        window.setOnCloseRequest(e -> System.exit(0));
        window.getIcons().add(new Image(LOGO));
        String relatedFxmlLandingPage = "ui/LandingPage.fxml";
        try {
            scene = new Scene(tools.loadFXML(Class.forName(APP_CLASS_NAME), relatedFxmlLandingPage));
        } catch (Exception e) {
            throw new FXMLLoaderException(relatedFxmlLandingPage);
        }
        setWindowSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        window.setScene(scene);
        window.show();
    }


    public static void changeScene(Scene scene) {
        window.setScene(scene);
        window.show();
    }

    private void setWindowSize(double height, double width) {
        window.setMinHeight(height);
        window.setMinWidth(width);
    }

    public static void main(String[] args) {
        launch();
    }

}