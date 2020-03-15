package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

public class FileFormatException extends Exception {

    public FileFormatException(){
        super();
        String outputMessage = "File name does not fulfill criteria. Check it out!";
        System.err.println(outputMessage);
    }
}
