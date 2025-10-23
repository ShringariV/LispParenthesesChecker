package org.example;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()) {
            System.out.println("Enter a LISP expression: ");
            String expression = scanner.nextLine();
            boolean isValid = LispParenthesesChecker.validateExpression(expression);
            if(isValid) {
                System.out.println("The expression is valid.");
            } else {
                System.out.println("The expression is invalid.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}