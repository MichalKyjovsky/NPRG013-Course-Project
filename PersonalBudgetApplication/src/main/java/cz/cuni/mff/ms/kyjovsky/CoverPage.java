package cz.cuni.mff.ms.kyjovsky;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CoverPage {
    private static final String COVER_PAGE_IMAGE_PATH = "/home/parzival/Java_Course/CourseProject/PersonalBudgetApplication/src/main/resources/cz/cuni/mff/ms/kyjovsky/appLogo.png";
    private static final int COVER_PAGE_IMG_WIDTH = 80;
    private static final int COVER_PAGE_IMG_HEIGHT = 80;

    public Scene displayCoverPage(Scene nextScene) {
        VBox vbox = new VBox();
        try {
            Image applicationLogo = new Image(new FileInputStream(COVER_PAGE_IMAGE_PATH));
            vbox.getChildren().addAll(setUpButton(nextScene), setUpIntroLabel(setImageProperty(applicationLogo)));
            return new Scene(vbox, App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        } catch (Exception e) {
            vbox.getChildren().add(setUpButton(nextScene));
            return new Scene(vbox, App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        }
    }
        private ImageView setImageProperty(Image selectedImage){
            ImageView imageView = new ImageView();
            imageView.setImage(selectedImage);
            imageView.setFitHeight(COVER_PAGE_IMG_HEIGHT);
            imageView.setFitWidth(COVER_PAGE_IMG_WIDTH);

            return imageView;
    }

        private Button setUpButton(Scene nextScene){
            Button appStartButton = new Button();
            appStartButton.setText("Entry");
            appStartButton.setOnAction(e -> App.stage.setScene(nextScene));

            return appStartButton;
        }

        private Label setUpIntroLabel(ImageView imgw){
            return new Label("Personal Budget Application",imgw);
        }
}
