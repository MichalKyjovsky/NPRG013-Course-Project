package cz.cuni.mff.ms.kyjovsm.exceptions;

public class WorkbookBuilderException extends Exception {
    public static final String FILE_SAVING_FAILED = "Error occurred during the workbook saving.";

    public WorkbookBuilderException(String causeOfException){
        super(causeOfException);
    }
}
