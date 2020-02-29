package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

public class FXMLLoaderException extends Exception {
    public FXMLLoaderException(){
        super();
        String err = "FXML Loader was not able to load particular file";
        System.err.println(err);
    }
}
