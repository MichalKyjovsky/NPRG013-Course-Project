package cz.cuni.mff.ms.kyjovsm.calculator;

import java.util.ArrayDeque;
import java.util.Deque;

 class LineProcessor {
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
      * Method will call method for transformation of given
      * String and return transformed expression to calling class.
      * @param input math expression to transform to postfix notation.
      * @return ERROR or math expression transformed in postfix notation.
      */
     String processLine(final String input) {
        String processedLine;
        if (!checkCorrectness(input)) {
            return ERROR_MESSAGE;
        } else {
            processedLine = separateNumbers(input);
            processedLine = postfixConversion(processedLine);
        }
        if (!processedLine.equals(" ")) {
            return processedLine;
        } else {
            return ERROR_MESSAGE;
        }
    }

     /**
      * Syntax analysis method.
      * @param input Expression string to transform.
      * @return String containing operands and operators in correct order.
      */
    private String separateNumbers(final String input) {
        String line = input.strip().trim().replaceAll(" ", "");
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char item = input.charAt(i);
            if ((item >= '0' && item <= '9')
                    || (item == 'e')
                    || (item == '.')) {
                buffer.append(item);
            } else if ((item >= 'a' && item <= 'z')
                    || (item >= 'A' && item <= 'Z')) {
                buffer.append(item);
            } else if (item == '-') {
                try {
                    if (buffer.length() > 0) {
                        char a = input.charAt(i - 1);
                        char b = input.charAt(i + 1);
                        if (((a == ')') || (a == '(') && (b >= 'A' && b <= 'Z'))
                                || ((a >= 'a' && a <= 'z') && (b == '(')
                                || (b == ')')) || ((a >= 'a' && a <= 'z')
                                && (b >= 'A' && b <= 'Z'))
                                || ((a >= '0' && a <= '9')
                                && (b >= '0' && b <= '9'))) {
                            if (buffer.charAt(buffer.length() - 1) != ' ') {
                                buffer.append(" ").append(item).append(" ");
                            } else {
                                buffer.append(item).append(" ");
                            }
                        } else if (((a >= 'a' && a <= 'z')
                                || (a >= 'A' && a <= 'Z'))
                                && ((b >= 'a' && b <= 'z')
                                || (b >= 'A' && b <= 'Z'))) {
                            if (buffer.charAt(buffer.length() - 1) != ' ') {
                                buffer.append(" ").append(item).append(" ");
                            } else {
                                buffer.append(item).append(" ");
                            }
                        } else if (a == '(' && b == '(') {
                            buffer.append("-1 * ");
                        } else if (a >= '0' && a <= '9' && b == '(') {
                            buffer.append(" ").append(item).append(" ");
                        } else {
                            buffer.append(item);
                        }
                    } else if (input.charAt(i + 1) == '(') {
                        buffer.append("-1 * ");
                    } else {
                        buffer.append(item);
                    }
                } catch (Exception e) {
                    buffer.append(item);
                }
            } else {
                if (buffer.length() > 0) {
                    if (buffer.charAt(buffer.length() - 1) != ' ') {
                        buffer.append(" ").append(item).append(" ");
                    } else {
                        buffer.append(item).append(" ");
                    }
                } else {
                    buffer.append(" ").append(item).append(" ");
                }
            }
        }
        return buffer.toString();
    }

     /**
      * Method validates that expression does not contain
      * incorrect operators or number format.
      * @param line math expression for check.
      * @return boolean value whether expression fulfill criteria.
      */
    private boolean checkCorrectness(final String line) {
        String input = line;

        if (input.isEmpty()) {
            return false;
        }

        if (containsNumber(input)
                && input.contains(" ")
                && !containsOperator(input)) {
            return false;
        }

        if (!containsNumber(input)
                && input.contains(" ")
                && !containsOperator(input)) {
            return false;
        }

        if (!containsNumber(input)
                && input.contains(" ")
                && containsOperator(input)) {
            return false;
        }

        input = input.strip().trim().replaceAll(" ", "");

        return !input.matches(".*[+-/*=][+-/*=]+.*");
    }

     /**
      * Method will transform expression to postfix.
      * @param input Expression to convert.
      * @return expression in postfix notation.
      */
    private String postfixConversion(final String input) {
        String[] formula = input.split(" ");
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new ArrayDeque<>();
        double number = 0;

        for (String item:formula) {
            if (item.isEmpty()) {
                continue;
            }
            if (item.matches(NUMBER_MATCHING_EXPRESSION)) {
                number = Double.parseDouble(item);
                output.append(number).append(" ");
            } else {
                switch (item) {
                    case "+":
                    case "-":
                        try {
                            if (stack.peek().equals("(") || stack.size() > 0) {
                                stack.push(item);
                            } else {
                                while (!(stack.peek().equals("(")
                                        || stack.size() > 0)) {
                                    if (!stack.peek().equals("(")) {
                                        output.append(stack.pop()).append(" ");
                                    }
                                }
                                stack.push(item);
                            }
                        } catch (Exception e1) {
                            stack.push(item);
                        }
                        break;
                    case "*":
                    case "/":
                        try {
                            if (!(stack.peek().equals("*")
                                    || (stack.peek().equals("/")))) {
                                stack.push(item);
                            } else {
                                while ((stack.peek().equals("*")
                                        || stack.peek().equals("/"))) {
                                    output.append(stack.pop()).append(" ");
                                }
                                stack.push(item);
                            }
                        } catch (Exception e1) {
                            stack.push(item);
                        }
                        break;
                    case "(":
                        stack.push(item);
                        break;
                    case ")":
                        try {
                            while (!stack.peek().equals("(")) {
                                if (!stack.peek().equals("(")) {
                                    output.append(stack.pop()).append(" ");
                                }
                            }
                            stack.pop();
                            break;
                        } catch (Exception e1) {
                            return ERROR_MESSAGE;
                        }
                    default:
                        output.append(item).append(" ");
                }
            }
        }
        while (stack.size() > 0) {
            output.append(stack.pop()).append(" ");
        }
        return output.toString();
    }

     /**
      * Method verifies, whether expression contains allowed operators.
      * @param input Math expression to check.
      * @return boolean value whether expression contains operators.
      */
     boolean containsOperator(final String input) {
        for (char item: input.toCharArray()) {
            switch (item) {
                case '+':
                case '-':
                case '/':
                case '*':
                case 'e':
                case '(':
                case ')':
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

     /**
      * Method verifies, whether expression contains allowed operands.
      * @param input Math expression to check.
      * @return boolean value whether expression contains operands.
      */
     boolean containsNumber(final String input) {
        for (char item: input.toCharArray()) {
            if (((item >= '0' && item <= '9')
                    || (item == 'e')
                    || (item == '.'))) {
                return true;
            }
        }
        return false;
    }

     /**
      * Method verifies, whether expression contains parentheses.
      * @param input Math expression to check.
      * @return boolean value whether expression contains parentheses.
      */
     boolean containsParenthesis(final String input) {
        for (char item : input.toCharArray()) {
            if (item == ')' || item == '(') {
                return true;
            }
        }
        return false;
    }
}


