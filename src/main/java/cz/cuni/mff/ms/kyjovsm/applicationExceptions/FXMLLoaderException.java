package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

public class FXMLLoaderException extends Exception {
    /**
     * Exception is invoked whenever unsuccessful attempt
     * to load .fxml in to scene is performed.
     * @param loadedFxml Variables stores which .fxml invoked error.
     */
    public FXMLLoaderException(final String loadedFxml) {
        super();
        System.err.println(
                String.format("FXML Loader was not able to load %s file",
                        loadedFxml));
    }
}
