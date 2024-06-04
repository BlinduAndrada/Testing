package org.example.testing;

public class ExampleTestClass {

    @Test
    public void testAddition() {
        int result = add(2, 3);
        assert result == 5 : "Addition test failed";
    }

    @Test
    public void testSubtraction() {
        int result = subtract(5, 3);
        assert result == 2 : "Subtraction test failed";
    }

    @Test
    public void testMultiplication() {
        int result = multiply(2, 3);
        assert result == 6 : "Multiplication test failed";
    }

    @Test
    public void testDivision() {
        try {
            int result = divide(6, 3);
            assert result == 2 : "Division test failed";
        } catch (ArithmeticException e) {
            assert false : "Division by zero should not occur in this test";
        }
    }

    @Test
    public void testDivisionByZero() {
        try {
            divide(1, 0);
            assert false : "Division by zero should have thrown an exception";
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test
    public void testStringConcatenation() {
        String result = concatenate("Hello, ", "world!");
        assert result.equals("Hello, world!") : "String concatenation test failed";
    }

    // Helper methods
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        return a / b;
    }

    public String concatenate(String a, String b) {
        return a + b;
    }
}
