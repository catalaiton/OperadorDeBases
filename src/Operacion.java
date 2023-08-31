import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.math.BigInteger;

public class Operacion {
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
            System.out.print("Ingrese un número en base " + base + ", o ingrese un operador (+, -, *, /), o 'q' para terminar: ");
            String input = scanner.next();

            if (input.equalsIgnoreCase("q")) {
                continueEnteringData = false;
            } else if (input.equals("+") || input.equals("-") || input.equals("*") || input.equals("/")) {
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
        String result = numbers.get(0);

        for (int i = 0; i < operations.size(); i++) {
            char operation = operations.get(i);
            String nextNumber = numbers.get(i + 1);

            switch (operation) {
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
                default:
                    System.out.println("Operador no válido.");
            }
        }

        return result;
    }

    public static String addInBase(String num1, String num2, int base) {
        return performOperation(num1, num2, base, '+');
    }

    public static String subtractInBase(String num1, String num2, int base) {
        int compareResult = customBaseToBase10(num1, base).compareTo(customBaseToBase10(num2, base));
        
        String largerNum = num1;
        String smallerNum = num2;

        if (compareResult < 0) {
            largerNum = num2;
            smallerNum = num1;
        }

        StringBuilder result = new StringBuilder();
        int borrow = 0;

        for (int i = 0; i < largerNum.length() || borrow != 0; i++) {
            int digit1 = i < largerNum.length() ? getValue(largerNum.charAt(largerNum.length() - 1 - i), base) : 0;
            int digit2 = i < smallerNum.length() ? getValue(smallerNum.charAt(smallerNum.length() - 1 - i), base) : 0;

            int tempResult = digit1 - digit2 - borrow;

            if (tempResult < 0) {
                tempResult += base;
                borrow = 1;
            } else {
                borrow = 0;
            }

            result.insert(0, getDigit(tempResult, base));
        }

        return result.toString();
    }

    public static String multiplyInBase(String num1, String num2, int base) {
        BigInteger base10Num1 = customBaseToBase10(num1, base);
        BigInteger base10Num2 = customBaseToBase10(num2, base);
        BigInteger productBase10 = base10Num1.multiply(base10Num2);

        return customBaseFromBase10(productBase10, base);
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
        String digits = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz" +
            "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワガ" +
            "ギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!¡#%&*∙\\'′″„" +
            "¨`´,./:;?¿@~-−_¯¦|‹›«∀∂∃∇∈∉$¸»ªâÂäÄÃãåÅæÆÇçÐðèÈéÉêÊëËƒìÌíÍîÎïÏⁿ" +
            "òÒóÓôÔöÖÕõØøŒœßÞþùÙúÚûÛüÜÿ™()[]{}+-×÷^=≠<>±≈≤≥₀¹²³⁴⁵⁶⁷⁸⁹‰¼½¾∞═│║┌╒╓╗" +
            "┐╕╖╗└╘╙╚┘╛╜╝├╞╟╠┤╡╢╣┬╤╥╦┴╧╨╩┼╪╫╬§©¬®°µ¶·♠♣♥♦†‡•αβΓγΔεζηΘθικΛλμνξΞ" +
            "πΠρΣσΣσςτυΦφχψΩωℵ⌂⌐⌠⌡◊↑→⇒↓←↔⇔─∏∑√∝∧∨∩∪∫∫∴≡⊂⊃⊆⊇БВГДЕЁЖЗИЙКЛМНОПР" +
            "СТУФХЦЧШЩЪЫЬЭЮЯбвгдежзийклмнопрстуфхцчшщъыьэюяё¹²³⁴⁵⁶⁷⁸⁹¼½¾∏ÅÆĄĆĘŁ" +
            "ŃÓŚŹŻąćęłńóśźż€₹";
        return digits.indexOf(digit);
    }

    public static char getDigit(int value, int base) {
        String digits = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz" +
            "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワガ" +
            "ギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!¡#%&*∙\\'′″„" +
            "¨`´,./:;?¿@~-−_¯¦|‹›«∀∂∃∇∈∉$¸»ªâÂäÄÃãåÅæÆÇçÐðèÈéÉêÊëËƒìÌíÍîÎïÏⁿ" +
            "òÒóÓôÔöÖÕõØøŒœßÞþùÙúÚûÛüÜÿ™()[]{}+-×÷^=≠<>±≈≤≥₀¹²³⁴⁵⁶⁷⁸⁹‰¼½¾∞═│║┌╒╓╗" +
            "┐╕╖╗└╘╙╚┘╛╜╝├╞╟╠┤╡╢╣┬╤╥╦┴╧╨╩┼╪╫╬§©¬®°µ¶·♠♣♥♦†‡•αβΓγΔεζηΘθικΛλμνξΞ" +
            "πΠρΣσΣσςτυΦφχψΩωℵ⌂⌐⌠⌡◊↑→⇒↓←↔⇔─∏∑√∝∧∨∩∪∫∫∴≡⊂⊃⊆⊇БВГДЕЁЖЗИЙКЛМНОПР" +
            "СТУФХЦЧШЩЪЫЬЭЮЯбвгдежзийклмнопрстуфхцчшщъыьэюяё¹²³⁴⁵⁶⁷⁸⁹¼½¾∏ÅÆĄĆĘŁ" +
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

    public static String customBaseFromBase10(BigInteger base10Value, int base) {
        StringBuilder result = new StringBuilder();

        while (base10Value.compareTo(BigInteger.ZERO) > 0) {
            BigInteger[] divRem = base10Value.divideAndRemainder(BigInteger.valueOf(base));
            int remainder = divRem[1].intValue();
            char digit = getDigit(remainder, base);
            result.insert(0, digit);
            base10Value = divRem[0];
        }

        return result.toString();
    }
}



