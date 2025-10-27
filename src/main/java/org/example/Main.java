package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("LISP Parentheses Validator");
        System.out.println("--------------------------");

        while (true) {
            System.out.print("Enter a LISP expression (or type 'exit' to quit): ");
            String expression = scanner.nextLine().trim();

            if (expression.equalsIgnoreCase("exit")) {
                break;
            }

            boolean isValid = LispParenthesesChecker.validateExpression(expression);

            if (isValid) {
                System.out.println("The expression is valid.\n");
            } else {
                System.out.println("The expression is invalid.\n");
            }
        }

        System.out.println("Goodbye!");
        scanner.close();
    }
}
