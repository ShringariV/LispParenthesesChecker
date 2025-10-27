package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LispParenthesesCheckerTest {

    @Test
    public void testValidSimple() {
        assertTrue(LispParenthesesChecker.validateExpression("(print (+ 1 2))"));
    }

    @Test
    public void testNestedValid() {
        assertTrue(LispParenthesesChecker.validateExpression("(defun square (x) (* x x))"));
    }

    @Test
    public void testUnbalancedLeft() {
        assertFalse(LispParenthesesChecker.validateExpression("((print 5)"));
    }

    @Test
    public void testUnbalancedRight() {
        assertFalse(LispParenthesesChecker.validateExpression("(print 5))"));
    }

    @Test
    public void testEmptyString() {
        assertTrue(LispParenthesesChecker.validateExpression(""));
    }

    @Test
    public void testNoParentheses() {
        assertTrue(LispParenthesesChecker.validateExpression("print 5"));
    }

    @Test
    public void testExtraClosingAtStart() {
        assertFalse(LispParenthesesChecker.validateExpression(")print("));
    }

    @Test
    public void testExtraClosingMiddle() {
        assertTrue(LispParenthesesChecker.validateExpression("(print ) ( (5))"));
    }

    @Test
    public void testOnlyOpening() {
        assertFalse(LispParenthesesChecker.validateExpression("("));
    }

    @Test
    public void testOnlyClosing() {
        assertFalse(LispParenthesesChecker.validateExpression(")"));
    }

    @Test
    public void testMisordered() {
        assertFalse(LispParenthesesChecker.validateExpression("())("));
    }

    @Test
    public void testMultipleInvalid() {
        assertFalse(LispParenthesesChecker.validateExpression("((print 5)))((("));
    }
    
    @Test
    public void testLongBalancedSequence() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) sb.append("(");
        for (int i = 0; i < 1000; i++) sb.append(")");
        assertTrue(LispParenthesesChecker.validateExpression(sb.toString()));
    }

    @Test
    public void testLongUnbalancedSequence() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) sb.append("(");
        for (int i = 0; i < 999; i++) sb.append(")");
        assertFalse(LispParenthesesChecker.validateExpression(sb.toString()));
    }

    @Test
    public void testParenthesesInsideStringLiterals() {
        assertTrue(LispParenthesesChecker.validateExpression("(print \"(this is text)\")"));
    }

    @Test
    public void testInvalidSpacingDoesNotAffectBalance() {
        assertTrue(LispParenthesesChecker.validateExpression("( print ( + ( * 2 3 ) 4 ) )"));
    }

    @Test
    public void testNonAsciiCharactersIgnored() {
        assertTrue(LispParenthesesChecker.validateExpression("(λ (x) (x x))"));
    }

    @Test
    public void testCommentLikeText() {
        assertFalse(LispParenthesesChecker.validateExpression("(print ; comment ) still counts (here))"));
    }

    @Test
    public void testAlternatingParenthesesInvalid() {
        assertFalse(LispParenthesesChecker.validateExpression("()()(()"));
    }

    @Test
    public void testComplexMixedCharactersInvalid() {
        assertFalse(LispParenthesesChecker.validateExpression("(defun x) ( (y (z))) )("));
    }

    @Test
    public void testLargeNestedInvalidOneMissing() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < 100; i++) sb.append("(");
        for (int i = 0; i < 99; i++) sb.append(")");
        assertFalse(LispParenthesesChecker.validateExpression(sb.toString()));
    }

    @Test
    public void testLargeNestedValid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 500; i++) sb.append("(");
        for (int i = 0; i < 500; i++) sb.append(")");
        assertTrue(LispParenthesesChecker.validateExpression(sb.toString()));
    }

    @Test
    public void testSpacesOnly() {
        assertTrue(LispParenthesesChecker.validateExpression("    "));
    }

    @Test
    public void testNewlinesTabsEtc() {
        assertTrue(LispParenthesesChecker.validateExpression("(\n\t(print (+ 1 2))\n)"));
    }


    @Test
    public void testLoneParenthesesInText() {
        assertTrue(LispParenthesesChecker.validateExpression("(hello) world (again)"));
    }

    @Test
    public void testInterleavedValidInvalid() {
        assertFalse(LispParenthesesChecker.validateExpression("(print (x)) ) (extra)"));
    }

    @Test
    public void testNestedSpacesInside() {
        assertTrue(LispParenthesesChecker.validateExpression("( ( ( ) ) )"));
    }

    @Test
    public void testParenthesesWithOperators() {
        assertTrue(LispParenthesesChecker.validateExpression("(+ (- (* 1 2) (/ 3 4)))"));
    }

    @Test
    public void testTrailingCharacters() {
        assertFalse(LispParenthesesChecker.validateExpression("(print 5))extra"));
    }

    @Test
    void testBalancedSimple() {
        assertTrue(LispParenthesesChecker.validateExpression("(a b)"));
    }

    @Test
    void testNestedBalanced() {
        assertTrue(LispParenthesesChecker.validateExpression("(a (b (c)))"));
    }

    @Test
    void testUnbalancedOpen() {
        assertFalse(LispParenthesesChecker.validateExpression("((a b)"));
    }

    @Test
    void testUnbalancedClose() {
        assertFalse(LispParenthesesChecker.validateExpression("(a b)))"));
    }


    @Test
    void testOnlyOpeningParentheses() {
        assertFalse(LispParenthesesChecker.validateExpression("((("));
    }

    @Test
    void testOnlyClosingParentheses() {
        assertFalse(LispParenthesesChecker.validateExpression(")))"));
    }

    @Test
    void testWhitespaceOnly() {
        assertTrue(LispParenthesesChecker.validateExpression("     "));
    }

    @Test
    void testLongValidExpression() {
        String expr = "(((((((((((((((((((())))))))))))))))))))";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testLongInvalidExpression() {
        String expr = "(((((((((((((((((((()))))))))))))))))))";
        assertFalse(LispParenthesesChecker.validateExpression(expr));
    }
    @Test
    void testEarlyCloseOrder() {
        assertFalse(LispParenthesesChecker.validateExpression(")("));
    }

    @Test
    void testInterleavedTextAndParentheses() {
        assertTrue(LispParenthesesChecker.validateExpression("(define (square x) (* x x))"));
    }

    @Test
    void testDeeplyNestedInvalid() {
        assertFalse(LispParenthesesChecker.validateExpression("((a(b(c))))("));
    }

    @Test
    void testEmptyStringIsBalanced() {
        assertTrue(LispParenthesesChecker.validateExpression(""), "Empty string should be balanced");
    }

    @Test
    void testNoParenthesesIsBalanced() {
        assertTrue(LispParenthesesChecker.validateExpression("hello world"), "No parentheses should be balanced");
    }

    @Test
    void testSimpleBalancedParentheses() {
        assertTrue(LispParenthesesChecker.validateExpression("(x + y)"));
        assertTrue(LispParenthesesChecker.validateExpression("{a[b(c)]}"));
    }

    @Test
    void testSimpleUnbalancedParentheses() {
        assertFalse(LispParenthesesChecker.validateExpression("("));
        assertFalse(LispParenthesesChecker.validateExpression("(x + y"));
        assertFalse(LispParenthesesChecker.validateExpression("([)]"), "Mismatched ordering should be invalid");
    }

    @Test
    void testBalancedWithNestedBrackets() {
        String expr = "({[()]})";
        assertTrue(LispParenthesesChecker.validateExpression(expr), "Properly nested brackets should be valid");
    }

    @Test
    void testUnbalancedWithExtraClosing() {
        assertFalse(LispParenthesesChecker.validateExpression("(()))"), "Extra closing bracket should invalidate");
    }

    @Test
    void testInvalidWhenBracketUnmatchedAcrossLines() {
        String expr = "(\n /* comment */";
        assertFalse(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testEscapedQuotesInsideString() {
        String expr = "\"This has an escaped quote \\\"(\\\"\" + (x)";
        assertTrue(LispParenthesesChecker.validateExpression(expr), "Escaped quotes shouldn't break string mode");
    }

    @Test
    void testUnbalancedParentheses() {
        assertFalse(LispParenthesesChecker.validateExpression("("));
        assertFalse(LispParenthesesChecker.validateExpression("(x + y"));
        assertFalse(LispParenthesesChecker.validateExpression("([)]"));
    }

    @Test
    void testBalancedNestedBrackets() {
        assertTrue(LispParenthesesChecker.validateExpression("({[()]})"));
    }

    @Test
    void testExtraClosingBracket() {
        assertFalse(LispParenthesesChecker.validateExpression("(()))"));
    }

    @Test
    void testBracketsInsideStringAreIgnored() {
        String expr = "\"(not real brackets)\" + (real)";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testBracketsInsideLispCommentAreIgnored() {
        String expr = "(x + y) ; comment with ( ) [ ] { }\n";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testMultipleLinesWithComments() {
        String expr = "(a)\n; comment line with )\n(b)";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testUnclosedStringStillCounts() {
        String expr = "\"(this never ends...\"";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testEscapedQuoteInsideString() {
        String expr = "\"this has an escaped quote \\\"(\\\")\" (x)";
        assertTrue(LispParenthesesChecker.validateExpression(expr));
    }

    @Test
    void testMockResultOverrideTrue() {
        LispParenthesesChecker.mockResult = true;
        assertTrue(LispParenthesesChecker.validateExpression("((((("));
        LispParenthesesChecker.mockResult = null;
    }

    @Test
    void testMockResultOverrideFalse() {
        LispParenthesesChecker.mockResult = false;
        assertFalse(LispParenthesesChecker.validateExpression("(((()"));
        LispParenthesesChecker.mockResult = null;
    }
}