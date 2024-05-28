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
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class) && Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
                System.out.println("Invoking @Test method: " + method.getName());
                method.setAccessible(true);
                method.invoke(null);
            }
        }
    }
}
