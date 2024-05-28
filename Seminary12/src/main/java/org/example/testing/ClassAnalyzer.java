package org.example.testing;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassAnalyzer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ClassAnalyzer <fully-qualified-class-name>");
            return;
        }

        String className = args[0];
        try {
            // Load the class
            Class<?> cls = Class.forName(className);

            // Extract information about the class
            extractClassInformation(cls);

            System.out.println();

            // Invoke static methods annotated with @Test
            invokeTestMethods(cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void extractClassInformation(Class<?> cls) {
        System.out.println("Class: " + cls.getName());

        // Print methods information
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method: " + method.toString());
        }
    }

    private static void invokeTestMethods(Class<?> cls) throws Exception {
        Method[] methods = cls.getDeclaredMethods();
        int testIndex = 0;
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class) && Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
                testIndex = runTest(method, testIndex);
            }
        }
    }

    private static int runTest(Method method, int testIndex) {
        testIndex++;
        System.out.println("Invoking @Test method: " + method.getName());
        method.setAccessible(true);

        boolean ok = true;

        try {
            method.invoke(null);
        } catch (Throwable e) {
            System.out.println("Test " + testIndex + " failed!");
            ok = false;
        }

        if (ok) {
            System.out.println("Test " + testIndex + " passed!");
        }

        System.out.println();

        return testIndex;
    }
}
