package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

class Calculator {
    private Map<String,Double> hashMap;
    private LineProcessor lineProcessor;
    private static final String ERROR_MESSAGE = "ERROR";
    private static final String LAST_VARIABLE = "last";
    private static final String NUMBER_MATCHING_EXPRESSION  =
            "-?\\d+(\\.\\d+)?";

     Calculator() {
        hashMap = new HashMap<String, Double>();
        lineProcessor = new LineProcessor();
        hashMap.put(LAST_VARIABLE, (double) 0);
    }

     String giveResults(String input) {
        if (input.contains("=")) {
            String eval = equation((input));
            if (!eval.equals(ERROR_MESSAGE)) {
                return String.format("%.5f", Double.parseDouble(eval));
            } else {
                return ERROR_MESSAGE;
            }
        } else if (input.equals(LAST_VARIABLE)) {
            return String.format("%.5f",hashMap.get(LAST_VARIABLE));
        } else {
            String output = expression(input);
            if (!output.equals(ERROR_MESSAGE)) {
                hashMap.replace(LAST_VARIABLE,Double.parseDouble(output));
                return String.format("%.5f", Double.parseDouble(output));
            } else {
                hashMap.replace(LAST_VARIABLE,(double)0);
                return ERROR_MESSAGE;
            }
        }
    }

    private String expression(String input) {
        String express = lineProcessor.processLine(input);
        if (express.equals(ERROR_MESSAGE)) {
            return ERROR_MESSAGE;
        } else {
            return calculate(express);
        }
    }

    private String equation(String input) {
        input = input.strip().trim().replaceAll(" ", "");
        String key = input.substring(0,input.indexOf('='));

        if (lineProcessor.containsOperator(key) || lineProcessor.containsNumber(key)) {
            return ERROR_MESSAGE;
        }

        String express = input.substring(input.indexOf('=') + 1);
        String value = "";
        value = lineProcessor.processLine(express);
        if (!value.equals(ERROR_MESSAGE)) {
            value = calculate(value);
        } else {
            return ERROR_MESSAGE;
        }

        if (hashMap.containsKey(key)) {
            hashMap.replace(key, Double.parseDouble(value));
        } else {
            hashMap.put(key,Double.parseDouble(value));
        }
        hashMap.replace(LAST_VARIABLE,Double.parseDouble(value));
        return value;
    }

    private String calculate(String input) {
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
                if (hashMap.containsKey(item)) {
                    result.push(hashMap.get(item));
                    continue;
                } else if (lineProcessor.isVariable(item)) {
                    hashMap.put(item, (double) 0);
                    result.push((double) 0);
                    continue;
                }
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
