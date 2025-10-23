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

// PsuedoCode
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
    public static boolean validateExpression(String expression) {
        Stack<Character> parenthesisStack = new Stack<>();
        for(int i = 0; i < expression.length(); i++) {
            if(expression.charAt(i) == '(') {
                parenthesisStack.push('(');
            } else if(expression.charAt(i) == ')') {
                if(parenthesisStack.isEmpty()) {
                    return false;
                } else {
                    parenthesisStack.pop();
                }
            }
        }
        return parenthesisStack.isEmpty();
    }
}
