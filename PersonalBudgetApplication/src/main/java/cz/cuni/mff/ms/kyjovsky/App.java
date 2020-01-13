package cz.cuni.mff.ms.kyjovsky;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    public static Stage stage;
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 300;

    public static final int MIN_APPLICATION_WIDTH = 200;
    public static final int MIN_APPLICATION_HEIGHT = 150;

    @Override
    public void start(Stage window) throws IOException {
        stage = window;
        stage.setMinHeight(MIN_APPLICATION_HEIGHT);
        stage.setMinWidth(MIN_APPLICATION_WIDTH);


        stage.setTitle("Personal budget application");

        stage.setScene(CoverPage.displayCoverPage());
        stage.show();

    }

}