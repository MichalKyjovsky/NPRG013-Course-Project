package cz.cuni.mff.ms.kyjovsm.additionalUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Tools {

    public Parent loadFXML(Class<?> cls, String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(cls.getResource(fxml));
        return fxmlLoader.load();
    }
}
