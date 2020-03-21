package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalcEntryPoint {

    private static Logger logger =
            Logger.getLogger(CalcEntryPoint.class.getName());

    public String calc(String input) {
        Calculator calculator = new Calculator();
        try {
            return calculator.giveResults(input);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Calculator failed.");
            return "ERROR";
        }
    }
}
