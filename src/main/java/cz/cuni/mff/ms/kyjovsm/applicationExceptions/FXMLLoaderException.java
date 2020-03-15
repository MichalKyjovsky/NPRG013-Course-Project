package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

public class FXMLLoaderException extends Exception {
    public FXMLLoaderException(String loadedFxml) {
        super();
        String err = String.format("FXML Loader was not able to load %s file",loadedFxml);
        System.err.println(err);
    }
}
