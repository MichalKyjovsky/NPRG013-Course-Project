package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

 class Calculator {
    private Map<String,Double> hashMap;
    private LineProcessor lineProcessor;

     Calculator() {
        hashMap = new HashMap<String, Double>();
        lineProcessor = new LineProcessor();
        hashMap.put("last", (double)0);
    }

     String giveResults(String input) {
        if (input.contains("=")) {
            String eval = equation((input));
            if (!eval.equals("ERROR")) {
                return String.format("%.5f", Double.parseDouble(eval));
            } else {
                return "ERROR";
            }
        } else if (input.equals("last")) {
            return String.format("%.5f",hashMap.get("last"));
        } else {
            String output = expression(input);
            if (!output.equals("ERROR")) {
                hashMap.replace("last",Double.parseDouble(output));
                return String.format("%.5f", Double.parseDouble(output));
            } else {
                hashMap.replace("last",(double)0);
                return "ERROR";
            }
        }
    }

    private String expression(String input) {
        String express = lineProcessor.processLine(input);
        if (express.equals("ERROR")) {
            return "ERROR";
        } else {
            return calculate(express);
        }
    }

    private String equation(String input) {
        input = input.strip().trim().replaceAll(" ", "");
        String key = input.substring(0,input.indexOf('='));

        if (lineProcessor.containsOperator(key) || lineProcessor.containsNumber(key)) {
            return "ERROR";
        }

        String express = input.substring(input.indexOf('=') + 1);
        String value = "";
        value = lineProcessor.processLine(express);
        if (!value.equals("ERROR")) {
            value = calculate(value);
        } else {
            return "ERROR";
        }

        if (hashMap.containsKey(key)) {
            hashMap.replace(key, Double.parseDouble(value));
        } else {
            hashMap.put(key,Double.parseDouble(value));
        }
        hashMap.replace("last",Double.parseDouble(value));
        return value;
    }

    private String calculate(String input) {
        String[] expression = input.split(" ");
        Stack<Double> result = new Stack<>();
        double a = 0;
        double b = 0;

        for (String item : expression) {
            if (item.equals("")) {
                continue;
            }
            try {
                if (hashMap.containsKey(item)) {
                    result.push(hashMap.get(item));
                    continue;
                } else if (lineProcessor.isVariable(item)) {
                    hashMap.put(item, (double)0);
                    result.push((double)0);
                    continue;
                }
                result.push(Double.parseDouble(item));
            } catch (Exception e) {
                try {
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
                                return "ERROR";
                            }
                            result.push(b / a);
                            break;
                        default:
                            return "ERROR";
                    }
                } catch (Exception e1) {
                    return  "ERROR";
                }
            }
        }
        return result.peek().toString();
    }
}
