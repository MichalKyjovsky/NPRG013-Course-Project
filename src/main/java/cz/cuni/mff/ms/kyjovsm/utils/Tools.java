package cz.cuni.mff.ms.kyjovsm.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class Tools {


    /**
     * Universal method for FXML Scene instance loading.
     * @param cls Class to which is particular fxml related
     * @param fxml Fxml related to the given Class cls
     * @return Parent node, which can be set
     * as a Scene instance and displayed in Stage instance
     * @throws IOException
     */
    public Parent loadFXML(final Class<?> cls,
                           final String fxml) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(cls.getClassLoader().getResource(fxml));
        return fxmlLoader.load();
    }
}
