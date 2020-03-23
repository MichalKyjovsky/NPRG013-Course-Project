package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalcEntryPoint {

    /**
     * An instance of class logger for creating debugging log messages.
     */
    private static Logger logger =
            Logger.getLogger(CalcEntryPoint.class.getName());
    /**
     * If user provides invalid expression, particular cell will
     * be filled with "ERROR" message.
     */
    private static final String ERROR_MESSAGE = "ERROR";

    /**
     * Error message raised if calculator fails.
     */
    private static final String CALC_ERROR = "Calculator failed.";

    /**
     * Method for calculating given expression
     * using class Calculator.java and LineProcessor.java.
     * @param input an expression to evaluate
     * @return result of the given expression
     */
    public String calc(final String input) {
        Calculator calculator = new Calculator();
        try {
            String out = calculator.giveResults(input);
            if (out.equals(ERROR_MESSAGE)) {
                return "0";
            } else {
                    return out;
                }
        } catch (Exception e) {
            logger.log(Level.SEVERE, CALC_ERROR);
            return "0";
        }
    }
}
