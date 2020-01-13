package cz.cuni.mff.ms.kyjovsky;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;

public class CoverPage {
    private static final String COVER_PAGE_IMAGE_PATH = "/home/parzival/Java_Course/CourseProject/PersonalBudgetApplication/src/main/resources/cz/cuni/mff/ms/kyjovsky/appLogo.png";
    private static final int COVER_PAGE_IMG_WIDTH = 80;
    private static final int COVER_PAGE_IMG_HEIGHT = 80;
    private static final String COVER_PAGE_BTN_TEXT = "ENTRY";
    private static final String COVER_PAGE_WELCOME_TEXT = "Personal Budget Application";

    public static Scene displayCoverPage() {
        VBox vbox = new VBox();
        ComponentBuilder componentBuilder = new ComponentBuilder();
        try {
            Image applicationLogo = new Image(new FileInputStream(COVER_PAGE_IMAGE_PATH));
            vbox.getChildren().addAll(componentBuilder.setUpButton(ModePage.displayModePage(), COVER_PAGE_BTN_TEXT),
                                      componentBuilder.setUpIntroLabel(componentBuilder.setImageProperty(applicationLogo,COVER_PAGE_IMG_WIDTH,COVER_PAGE_IMG_HEIGHT), COVER_PAGE_WELCOME_TEXT));

            vbox.setAlignment(Pos.CENTER);
            return new Scene(vbox, App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        } catch (Exception e) {
            vbox.getChildren().add(componentBuilder.setUpButton(ModePage.displayModePage(),COVER_PAGE_BTN_TEXT));
            vbox.setAlignment(Pos.CENTER);
            return new Scene(vbox, App.APPLICATION_WIDTH, App.APPLICATION_HEIGHT);
        }
    }

}
