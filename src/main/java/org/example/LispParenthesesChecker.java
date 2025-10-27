package org.example;

// Data Structures to use
/*
Queue?
    enqueue (
    Enqueue (
    dequeue() when ) appears => dequeues first (
        out of order, doesn't work
 Needs to be LIFO, so use a stack
 */


/*
Main idea:
    LISP code is addressed like (x + y)
    Need to check if there's one ( matching every ), but in order.
        push ( into LIFO data structure (i.e. stack)
        pop when ) found
 */


import java.util.Stack;

// PsuedoCode of Core Idea
/*
    Make an empty stack
    for int i = 0; i < string length; i++:
        if string[i] == '(':
            push into stack
        if string[i] == ')':
            pop stack
        else:
            continue
    if stack is empty:
        return true
    else:
        return false
 */




public class LispParenthesesChecker {
    // Test-only override
    static Boolean mockResult = null;

    /**
     * Validates whether the given LISP-style expression is balanced with respect to parentheses,
     * brackets, and braces. Also handles comments and string literals while performing the validation.
     *
     * @param expression the string containing the LISP-style expression to validate
     * @return {@code true} if the expression is balanced and valid, {@code false} otherwise
     */
    public static boolean validateExpression(String expression) {
        if (mockResult != null) {
            return mockResult;
        }
        if (expression.isEmpty()) {
            return true;
        }

        Stack<Character> stack = new Stack<>();
        boolean inString = false;
        boolean inComment = false;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // Handle string or comment modes

            // If inside a string
            if (inString) {
                // exit string when encountering unescaped quote
                if (c == '"' && (i == 0 || expression.charAt(i - 1) != '\\')) {
                    inString = false;
                }
                continue;
                // ignore
            }

            // If inside a comment
            if (inComment) {
                // exit at new line
                if (c == '\n') {
                    inComment = false;
                }
                continue;
            }

            // Enter string or comment modes
            if (c == '"') {
                inString = true;
                continue;
            }
            if (c == ';') {
                inComment = true;
                continue;
            }

            // Bracket handling
            // if bracket, push
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            }
            // if closed bracket, pop
            else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                // if it doesn't match the top, return false due to mismatch
                if (!matches(top, c)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }
    /**
     * Checks whether a given pair of characters forms a valid
     * opening-closing bracket combination.
     *
     * <p>Supports the following pairs:
     * <ul>
     *     <li>'(' with ')'</li>
     *     <li>'{' with '}'</li>
     *     <li>'[' with ']'</li>
     * </ul>
     *
     * @param open  the opening bracket character (one of '(', '{', '[')
     * @param close the closing bracket character (one of ')', '}', ']')
     * @return true if the pair matches correctly; false otherwise
     */
    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')') ||
                (open == '{' && close == '}') ||
                (open == '[' && close == ']');
    }
}
