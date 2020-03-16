package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

public class FileFormatException extends Exception {

    /**
     * Exception is invoked when Workbook instance was not
     * created correctly.
     */
    public FileFormatException() {
        super();
        String outputMessage =
                "File name does not fulfill criteria. Check it out!";
        System.err.println(outputMessage);
    }
}
