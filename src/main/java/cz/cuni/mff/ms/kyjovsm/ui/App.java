package cz.cuni.mff.ms.kyjovsm.ui;

import cz.cuni.mff.ms.kyjovsm.utils.Tools;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {


    /**
     * Instance of Stage class which serve for displaying
     * of main view of the application.
     */
    private static Stage window;

    /**
     * Instance of the Scene class providing initial
     * view displayed when application starts.
     */
    private Scene  scene;

    /**
     * Variable storing path to the application logo icon.
     */
    private static final String LOGO = "pics_source/vault.png";
    /**
     * Full class name of the App.java.
     */
    private static final String APP_CLASS_NAME =
            "cz.cuni.mff.ms.kyjovsm.ui.App";
    /**
     * Initial window height constant.
     */
    private static final int WINDOW_HEIGHT = 660;
    /**
     * Initial window width constant.
     */
    private static final int WINDOW_WIDTH = 950;


    /**
     * Initial application method invokes the application Stage.
     * @param stage main Stage instance on which all application
     *              elements will be displayed
     */
    @Override
    public void start(final Stage stage) {
        Tools tools = new Tools();
        window = stage;
        window.setOnCloseRequest(e -> {
            System.exit(0);
            try {
                SheetBuilderController.getBudgetTracker().close();
            } catch (IOException ioe) {
                // CAN BE IGNORED, CLOSE REQUEST
            }
        });
        window.getIcons().add(new Image(LOGO));
        String relatedFxmlLandingPage = "ui/LandingPage.fxml";
        try {
            scene = new Scene(tools.
                    loadFXML(Class.forName(APP_CLASS_NAME),
                            relatedFxmlLandingPage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setWindowSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        window.setScene(scene);
        window.show();
    }



    /**
     * @param scene Loaded .fxml as a instance of Scene class
     *              for changing application Stage instance
     *              window view.
     */
    public static void changeScene(final Scene scene) {
        window.setScene(scene);
        window.show();
    }


    /**
     * Method for initialize Stage instance window size.
     * @param height preferred height
     * @param width preferred width
     */
    private void setWindowSize(final double height, final double width) {
        window.setMinHeight(height);
        window.setMinWidth(width);
    }


    /**
     * Application invocation method.
     * @param args main declaration
     */
    public static void main(final String[] args) {
        launch();
    }
}
