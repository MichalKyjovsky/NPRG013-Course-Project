package cz.cuni.mff.ms.kyjovsky;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ComponentBuilder {
    public ImageView setImageProperty(Image selectedImage, int width, int height){
        ImageView imageView = new ImageView();
        imageView.setImage(selectedImage);
        imageView.setFitHeight(height);
        imageView.setFitWidth(width);

        return imageView;
    }

    public Button setUpButton(Scene nextScene, String btnText){
        Button appStartButton = new Button();
        appStartButton.setText(btnText);
        appStartButton.setOnAction(e -> App.stage.setScene(nextScene));

        return appStartButton;
    }

    public Label setUpIntroLabel(ImageView imgw, String welcomeText){
        return new Label(welcomeText,imgw);
    }
}
