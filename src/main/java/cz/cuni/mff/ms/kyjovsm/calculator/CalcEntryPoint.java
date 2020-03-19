package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CalcEntryPoint {

    public String calc(String input) {
        Logger logger = Logger.getLogger(CalcEntryPoint.class.getName());
        Calculator calculator = new Calculator();
	    try {
	            return calculator.giveResults(input);

        } catch(Exception e) {
	        logger.log(Level.SEVERE, "Calculator failed.");
	        return "ERROR";
        }
    }
}
