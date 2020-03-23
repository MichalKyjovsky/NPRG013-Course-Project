package cz.cuni.mff.ms.kyjovsm;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalcEntryPoint {

    /**
     * An instance of class logger for creating debugging log messages.
     */
    private static Logger logger =
            Logger.getLogger(CalcEntryPoint.class.getName());

    /**
     * Method for calculating given expression
     * using class Calculator.java and LineProcessor.java.
     * @param input an expression to evaluate
     * @return result of the given expression
     */
    public String calc(final String input) {
        Calculator calculator = new Calculator();
        try {
            return calculator.giveResults(input);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Calculator failed.");
            return "ERROR";
        }
    }
}
