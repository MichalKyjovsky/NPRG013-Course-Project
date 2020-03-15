package cz.cuni.mff.ms.kyjovsm.applicationExceptions;

class InputFieldException extends Exception {
    public InputFieldException() {
        super();
        String err = "Input field does not work properly";
        System.err.println(err);
    }
}
