import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.math.BigInteger;

public class Operacion2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la base numérica (entre 2 y 1024): ");
        int base = scanner.nextInt();

        if (base < 2 || base > 1024) {
            System.out.println("La base numérica debe estar entre 2 y 1024.");
            return;
        }

        List<String> numbers = new ArrayList<>();
        List<Character> operations = new ArrayList<>();

        boolean continueEnteringData = true;

        while (continueEnteringData) {
            System.out.print("Ingrese un número en base " + base + ", un operador (+, -, *, /), '(' o ')' o 'q' para terminar: ");
            String input = scanner.next();

            if (input.equalsIgnoreCase("q")) {
                continueEnteringData = false;
            } else if (input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/") || input.equals("(") || input.equals(")")) {
                operations.add(input.charAt(0));
            } else {
                numbers.add(input);
            }
        }

        scanner.close();

        String result = performOperationsInBase(numbers, operations, base);

        System.out.println("Resultado en base " + base + ": " + result);

        if (compareResults(result, numbers, operations, base)) {
            System.out.println("Los resultados en bases coinciden.");
        } else {
            System.out.println("Los resultados en bases no coinciden.");
        }
    }

    public static String performOperationsInBase(List<String> numbers, List<Character> operations, int base) {
        List<String> simplifiedExpression = simplifyExpression(numbers, operations, base);
        String result = evaluateExpression(simplifiedExpression, base);
        return result;
    }

    public static List<String> simplifyExpression(List<String> numbers, List<Character> operations, int base) {
        List<String> simplified = new ArrayList<>();
        int index = 0;
        while (index < numbers.size()) {
            String token = numbers.get(index);
            if (token.equals("(")) {
                List<String> subExpression = new ArrayList<>();
                int openCount = 1;
                index++; // Move to next token after '('
                while (openCount > 0 && index < numbers.size()) {
                    String subToken = numbers.get(index);
                    if (subToken.equals("(")) {
                        openCount++;
                    } else if (subToken.equals(")")) {
                        openCount--;
                    }
                    if (openCount > 0) {
                        subExpression.add(subToken);
                    }
                    index++;
                }
                String subResult = evaluateExpression(subExpression, base);
                simplified.add(subResult);
            } else {
                simplified.add(token);
                index++;
            }
        }
        return simplified;
    }

    public static String evaluateExpression(List<String> expression, int base) {
        String result = expression.get(0);

        for (int i = 0; i < expression.size() - 1; i += 2) {
            char operator = expression.get(i + 1).charAt(0);
            String nextNumber = expression.get(i + 2);

            switch (operator) {
                case '+':
                    result = addInBase(result, nextNumber, base);
                    break;
                case '-':
                    result = subtractInBase(result, nextNumber, base);
                    break;
                case '*':
                    result = multiplyInBase(result, nextNumber, base);
                    break;
                case '/':
                    result = divideInBase(result, nextNumber, base);
                    break;
            }
        }

        return result;
    }

    public static String addInBase(String num1, String num2, int base) {
        return performOperation(num1, num2, base, '+');
    }

    public static String subtractInBase(String num1, String num2, int base) {
        return performOperation(num1, num2, base, '-');
    }

    public static String multiplyInBase(String num1, String num2, int base) {
        return performOperation(num1, num2, base, '*');
    }

    public static String divideInBase(String num1, String num2, int base) {
        return performOperation(num1, num2, base, '/');
    }

    public static String performOperation(String num1, String num2, int base, char operator) {
        int maxLength = Math.max(num1.length(), num2.length());
        StringBuilder result = new StringBuilder();
        int carry = 0;

        for (int i = 0; i < maxLength || carry != 0; i++) {
            int digit1 = i < num1.length() ? getValue(num1.charAt(num1.length() - 1 - i), base) : 0;
            int digit2 = i < num2.length() ? getValue(num2.charAt(num2.length() - 1 - i), base) : 0;

            int tempResult = 0;
            if (operator == '+') {
                tempResult = digit1 + digit2 + carry;
            } else if (operator == '-') {
                tempResult = digit1 - digit2 + carry;
            } else if (operator == '*') {
                tempResult = digit1 * digit2 + carry;
            } else if (operator == '/') {
                tempResult = digit1 / digit2 + carry;
            }

            carry = tempResult / base;
            result.insert(0, getDigit(tempResult % base, base));
        }

        return result.toString();
    }

    public static int getValue(char digit, int base) {
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"+
        				"アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワガ"+
        				"ギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!¡#%&*∙\\'′″„"+
        				"¨`´,./:;?¿@~-−_¯¦|‹›«∀∂∃∇∈∉$¸»ªâÂäÄÃãåÅæÆÇçÐðèÈéÉêÊëËƒìÌíÍîÎïÏⁿ"+
        				"òÒóÓôÔöÖÕõØøŒœßÞþùÙúÚûÛüÜÿ™()[]{}+-×÷^=≠<>±≈≤≥₀¹²³⁴⁵⁶⁷⁸⁹‰¼½¾∞═│║┌╒╓╗"+
        				"┐╕╖╗└╘╙╚┘╛╜╝├╞╟╠┤╡╢╣┬╤╥╦┴╧╨╩┼╪╫╬§©¬®°µ¶·♠♣♥♦†‡•αβΓγΔεζηΘθικΛλμνξΞ"+
        				"πΠρΣσΣσςτυΦφχψΩωℵ⌂⌐⌠⌡◊↑→⇒↓←↔⇔─∏∑√∝∧∨∩∪∫∫∴≡⊂⊃⊆⊇БВГДЕЁЖЗИЙКЛМНОПР"+
        				"СТУФХЦЧШЩЪЫЬЭЮЯбвгдежзийклмнопрстуфхцчшщъыьэюяё¹²³⁴⁵⁶⁷⁸⁹¼½¾∏ÅÆĄĆĘŁ"+
        				"ŃÓŚŹŻąćęłńóśźż€₹";
        return digits.indexOf(digit);
    }

    public static char getDigit(int value, int base) {
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"+
				"アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワガ"+
				"ギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!¡#%&*∙\\'′″„"+
				"¨`´,./:;?¿@~-−_¯¦|‹›«∀∂∃∇∈∉$¸»ªâÂäÄÃãåÅæÆÇçÐðèÈéÉêÊëËƒìÌíÍîÎïÏⁿ"+
				"òÒóÓôÔöÖÕõØøŒœßÞþùÙúÚûÛüÜÿ™()[]{}+-×÷^=≠<>±≈≤≥₀¹²³⁴⁵⁶⁷⁸⁹‰¼½¾∞═│║┌╒╓╗"+
				"┐╕╖╗└╘╙╚┘╛╜╝├╞╟╠┤╡╢╣┬╤╥╦┴╧╨╩┼╪╫╬§©¬®°µ¶·♠♣♥♦†‡•αβΓγΔεζηΘθικΛλμνξΞ"+
				"πΠρΣσΣσςτυΦφχψΩωℵ⌂⌐⌠⌡◊↑→⇒↓←↔⇔─∏∑√∝∧∨∩∪∫∫∴≡⊂⊃⊆⊇БВГДЕЁЖЗИЙКЛМНОПР"+
				"СТУФХЦЧШЩЪЫЬЭЮЯбвгдежзийклмнопрстуфхцчшщъыьэюяё¹²³⁴⁵⁶⁷⁸⁹¼½¾∏ÅÆĄĆĘŁ"+
				"ŃÓŚŹŻąćęłńóśźż€₹";
        return digits.charAt(value);
    }

    public static boolean compareResults(String result, List<String> numbers, List<Character> operations, int base) {
        String computedResult = performOperationsInBase(numbers, operations, base);

        BigInteger base10Computed = customBaseToBase10(computedResult, base);
        BigInteger base10Result = customBaseToBase10(result, base);

        System.out.println("Resultado en base 10 calculado: " + base10Computed);
        System.out.println("Resultado en base 10 esperado: " + base10Result);

        return base10Computed.equals(base10Result);
    }

    public static BigInteger customBaseToBase10(String baseValue, int base) {
        BigInteger base10Value = BigInteger.ZERO;

        for (int i = 0; i < baseValue.length(); i++) {
            char digit = baseValue.charAt(i);
            int digitValue = getValue(digit, base);
            base10Value = base10Value.multiply(BigInteger.valueOf(base)).add(BigInteger.valueOf(digitValue));
        }

        return base10Value;
    }
}




