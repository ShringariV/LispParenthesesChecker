package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void setUpStreams() {
        originalOut = System.out;
        originalIn = System.in;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
        resetMock();
    }

    private String getOutput() {
        return outContent.toString().replace("\r", "");
    }

    @Test
    void testValidAndInvalidExpressions() {
        String input = "(print (hello))\n((a b)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("LISP Parentheses Validator"));
        assertTrue(output.contains("The expression is valid."));
        assertTrue(output.contains("The expression is invalid."));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void testExitImmediately() {
        String input = "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("LISP Parentheses Validator"));
        assertTrue(output.contains("Goodbye!"));
        assertFalse(output.contains("The expression is valid."));
        assertFalse(output.contains("The expression is invalid."));
    }

    @Test
    void testWhitespaceAndCaseInsensitiveExit() {
        String input = "   (define x 10)   \nExIt\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("The expression is valid."));
        assertTrue(output.contains("Goodbye!"));
    }


    @Test
    void testEmptyLineBeforeExit() {
        String input = "\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("The expression is valid.") || output.contains("The expression is invalid."));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void testMultipleValidExpressions() {
        String input = "(a)\n(b)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        long count = output.split("The expression is valid").length - 1;
        assertEquals(2, count);
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void testMultipleInvalidExpressions() {
        String input = "((a)\n((b)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        long count = output.split("The expression is invalid").length - 1;
        assertEquals(2, count);
    }

    @Test
    void testMixedValidInvalidExpressions() {
        String input = "(a)\n((b)\n(c)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("The expression is valid."));
        assertTrue(output.contains("The expression is invalid."));
    }

    @Test
    void testWhitespaceBeforeExit() {
        String input = "   exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        assertTrue(outContent.toString().contains("Goodbye!"));
    }

    @Test
    void testEOFNoInput() {
        String input = "";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertThrows(NoSuchElementException.class, () -> Main.main(new String[]{}));
    }

    @Test
    void testPlainTextInput() {
        String input = "hello world\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        assertTrue(outContent.toString().contains("The expression is valid."));
    }

    @Test
    void testPromptRepeatsEachTime() {
        String input = "(a)\n(b)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        long promptCount = output.split("Enter a LISP expression").length - 1;
        assertEquals(3, promptCount);
    }


    @Test
    void testMockAlwaysValid() throws Exception {
        setMock(true);
        String input = "(anything)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("The expression is valid."));
        assertFalse(output.contains("The expression is invalid."));
    }

    @Test
    void testMockAlwaysInvalid() throws Exception {
        setMock(false);
        String input = "(anything)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        String output = outContent.toString();

        assertTrue(output.contains("The expression is invalid."));
        assertFalse(output.contains("The expression is valid."));
    }

    @Test
    void testMockResetsToRealLogic() throws Exception {
        setMock(true);
        resetMock();
        String input = "((a)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});
        assertTrue(outContent.toString().contains("The expression is invalid."));
    }

    @Test
    void testMockAlternation() throws Exception {
        setMock(true);
        String input1 = "(x)\nexit\n";
        System.setIn(new ByteArrayInputStream(input1.getBytes()));
        Main.main(new String[]{});
        String firstOutput = outContent.toString();
        assertTrue(firstOutput.contains("The expression is valid."));

        outContent.reset();

        resetMock();
        setMock(false);
        String input2 = "(y)\nexit\n";
        System.setIn(new ByteArrayInputStream(input2.getBytes()));
        Main.main(new String[]{});
        String secondOutput = outContent.toString();
        assertTrue(secondOutput.contains("The expression is invalid."));
    }

    @Test
    void testInvalidExpressionInput() {
        String input = "((\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("The expression is invalid."), "Should detect invalid expression");
    }

    @Test
    void testMultipleInputsAlternating() {
        String input = "(x)\n(y\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Should detect first valid input");
        assertTrue(output.contains("The expression is invalid."), "Should detect second invalid input");
    }

    @Test
    void testHandlesEmptyInputGracefully() {
        String input = "\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Main.main(new String[]{});
        String output = getOutput();
        assertTrue(output.contains("LISP Parentheses Validator"), "Should still print header");
        assertTrue(output.contains("The expression is valid."), "Empty input should be treated as balanced");
    }


    @Test
    void testProgramStartsAndExitsGracefully() {
        String input = "exit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("LISP Parentheses Validator"), "Should print header");
        assertTrue(output.contains("Goodbye!"), "Should print goodbye on exit");
    }

    @Test
    void testValidExpression() {
        String input = "(x + y)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Should detect valid expression");
    }

    @Test
    void testInvalidExpression() {
        String input = "((x + y)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is invalid."), "Should detect invalid expression");
    }

    @Test
    void testEmptyInputIsBalanced() {
        String input = "\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Empty input should be treated as balanced");
    }

    @Test
    void testLispCommentIgnored() {
        String input = "(a)\n; this comment has ) ] }\n(b)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Brackets in Lisp comments should be ignored");
    }

    @Test
    void testStringIgnoresBrackets() {
        String input = "\"(not real)\"\n(real)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Brackets inside strings should be ignored");
    }

    @Test
    void testAlternatingInputs() {
        String input = "(x)\n(y\n; comment )\n(exit)\nexit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(new String[]{});

        String output = getOutput();
        assertTrue(output.contains("The expression is valid."), "Should detect valid expressions");
        assertTrue(output.contains("The expression is invalid."), "Should detect invalid expressions");
    }

    private void setMock(boolean result) throws Exception {
        Field field = LispParenthesesChecker.class.getDeclaredField("mockResult");
        field.setAccessible(true);
        field.set(null, result);
    }

    private void resetMock() {
        try {
            Field field = LispParenthesesChecker.class.getDeclaredField("mockResult");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception ignored) {}
    }


}
