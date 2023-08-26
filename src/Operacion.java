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
        String digits = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz"
        		+"アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワ"
        		+"ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!";
        return digits.indexOf(digit);
    }

    public static char getDigit(int value, int base) {
        String digits = "0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz"
        		+"アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワ"
        		+"ガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎ!";
        
        return digits.charAt(value);
    }

    public static boolean compareResults(String result, List<String> numbers, List<Character> operations, int base) {
        String resultInBase10 = convertToBase10(result, base);
        String expectedResultInBase10 = calculateExpectedResult(numbers, operations, base);

        System.out.println("Resultado en base 10: " + resultInBase10);
        System.out.println("Resultado esperado en base 10: " + expectedResultInBase10);

        return resultInBase10.equals(expectedResultInBase10);
    }

    public static String calculateExpectedResult(List<String> numbers, List<Character> operations, int base) {
        BigInteger base10Result = new BigInteger(convertToBase10(numbers.get(0), base));

        for (int i = 0; i < operations.size(); i++) {
            char operation = operations.get(i);
            BigInteger nextNumberInBase = new BigInteger(convertToBase10(numbers.get(i + 1), base));

            if (operation == '+') {
                base10Result = base10Result.add(nextNumberInBase);
            } else if (operation == '-') {
                base10Result = base10Result.subtract(nextNumberInBase);
            } else if (operation == '*') {
                base10Result = base10Result.multiply(nextNumberInBase);
            } else if (operation == '/') {
                base10Result = base10Result.divide(nextNumberInBase);
            }
        }
        return base10Result.toString();
    }

    public static String convertToBase10(String number, int base) {
        BigInteger base10Number = BigInteger.ZERO;
        BigInteger multiplier = BigInteger.ONE;

        for (int i = number.length() - 1; i >= 0; i--) {
            char digit = number.charAt(i);
            int digitValue = getValue(digit, base);
            base10Number = base10Number.add(multiplier.multiply(BigInteger.valueOf(digitValue)));
            multiplier = multiplier.multiply(BigInteger.valueOf(base));
        }

        return base10Number.toString();
    }
}


