package cz.cuni.mff.ms.kyjovsm;

import java.util.ArrayDeque;
import java.util.Deque;

class Calculator {
    /**
     * Instance of LineProcessor.java for transforming given
     * input in the POSTFIX notation.
     */
    private LineProcessor lineProcessor;
    /**
     * If user provides invalid expression, particular cell will
     * be filled with "ERROR" message.
     */
    private static final String ERROR_MESSAGE = "ERROR";
    /**
     * Regular expression matching any Integer or Float number.
     */
    private static final String NUMBER_MATCHING_EXPRESSION  =
            "-?\\d+(\\.\\d+)?";

    /**
     * Constructor of Calculator class.
     */
     Calculator() {
        lineProcessor = new LineProcessor();
    }

    /**
     * Main method of the class which will process the
     * given input.
     * @param input String expression to evaluate
     * @return result or the "ERROR" message
     */
     String giveResults(final String input) {
        String output = expression(input);
            if (!output.equals(ERROR_MESSAGE)) {
                return String.format("%.5f", Double.parseDouble(output));
            } else {
                return ERROR_MESSAGE;
            }
        }

    /**
     * Method will call method of class LineProcessor
     * to transform input to postfix notation and
     * than redirect result of previous method to
     * calculate() method.
     * @param input String expression to evaluate
     * @return result or the "ERROR" message
     */
    private String expression(final String input) {
        String express = lineProcessor.processLine(input);
        if (express.equals(ERROR_MESSAGE)) {
            return ERROR_MESSAGE;
        } else {
            return calculate(express);
        }
    }

    /**
     * Method will evaluate given math expression in postfix notation.
     * @param input math expression in postfix notation
     * @return result of the expression or "ERROR" message
     */
    private String calculate(final String input) {
        String[] expression = input.split(" ");
        Deque<Double> result = new ArrayDeque<>();
        double a = 0;
        double b = 0;
        double tryValue = 0;
        boolean isDouble = true;

        for (String item : expression) {
            isDouble = true;

            if (item.isEmpty()) {
                continue;
            }
            if (item.matches(NUMBER_MATCHING_EXPRESSION)) {
                result.push(Double.parseDouble(item));
            } else {
                    switch (item) {
                        case "+":
                            a = result.pop();
                            b = result.pop();
                            result.push(a + b);
                            break;
                        case "-":
                            a = result.pop();
                            b = result.pop();
                            result.push(b - a);
                            break;
                        case "*":
                            a = result.pop();
                            b = result.pop();
                            result.push(a * b);
                            break;
                        case "/":
                            a = result.pop();
                            b = result.pop();

                            if (a == 0) {
                                return ERROR_MESSAGE;
                            }
                            result.push(b / a);
                            break;
                        default:
                            return ERROR_MESSAGE;
                  }
            }
        }
        return result.peek().toString();
    }
}
