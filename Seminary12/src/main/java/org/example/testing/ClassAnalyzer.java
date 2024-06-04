package org.example.testing;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class ClassAnalyzer {

    private static int totalTests = 0;
    private static int successfulTests = 0;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java org.example.testing.ClassAnalyzer <path-to-class-or-folder-or-jar>");
            return;
        }

        String path = args[0];
        File file = new File(path);

        List<Class<?>> classes = new ArrayList<>();

        try {
            if (file.isDirectory()) {
                exploreDirectory(file, classes);
            } else if (file.getName().endsWith(".jar")) {
                exploreJar(file, classes);
            } else if (file.getName().endsWith(".class")) {
                classes.add(loadClassFromFile(file));
            }

            for (Class<?> cls : classes) {
                if (cls != null) {
                    extractClassInformation(cls);
                    invokeTestMethods(cls);
                }
            }

            printStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void exploreDirectory(File dir, List<Class<?>> classes) throws IOException, ClassNotFoundException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                exploreDirectory(file, classes);
            } else if (file.getName().endsWith(".class")) {
                classes.add(loadClassFromFile(file));
            }
        }
    }

    private static void exploreJar(File jarFile, List<Class<?>> classes) throws IOException, ClassNotFoundException {
        try (JarFile jar = new JarFile(jarFile)) {
            URL[] urls = {new URL("jar:file:" + jarFile.getAbsolutePath() + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            jar.stream()
                    .filter(entry -> entry.getName().endsWith(".class"))
                    .forEach(entry -> {
                        String className = entry.getName().replace('/', '.').replace(".class", "");
                        try {
                            classes.add(cl.loadClass(className));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    private static Class<?> loadClassFromFile(File file) throws IOException, ClassNotFoundException {
        String className = file.getPath()
                .replace(System.getProperty("user.dir") + File.separator, "")
                .replace(File.separator, ".")
                .replace(".class", "");
        System.out.println(className);
        URL[] urls = {file.getParentFile().toURI().toURL()};
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        return cl.loadClass(className);
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
            if (method.isAnnotationPresent(Test.class)) {
                totalTests++;
                try {
                    if (Modifier.isStatic(method.getModifiers()) && method.getParameterCount() == 0) {
                        method.setAccessible(true);
                        method.invoke(null);
                    } else {
                        Object instance = cls.getDeclaredConstructor().newInstance();
                        Object[] mockValues = generateMockValues(method.getParameterTypes());
                        method.setAccessible(true);
                        method.invoke(instance, mockValues);
                    }
                    successfulTests++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Object[] generateMockValues(Class<?>[] parameterTypes) {
        Object[] mockValues = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == int.class) {
                mockValues[i] = 0;
            } else if (parameterTypes[i] == String.class) {
                mockValues[i] = "";
            }
            // Add more cases for other primitive types if needed
        }
        return mockValues;
    }

    private static void printStatistics() {
        System.out.println("Total tests: " + totalTests);
        System.out.println("Successful tests: " + successfulTests);
    }
}
