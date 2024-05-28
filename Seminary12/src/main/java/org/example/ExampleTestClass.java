package org.example;

import org.example.testing.Test;

public class ExampleTestClass {

    @Test
    public static void foo1() {
        System.out.println(1);
    }

    public static void foo2() {
        System.out.println(2);
    }

    @Test
    public static void foo3() {
        System.out.println(3);
        throw new RuntimeException("BOOOOOOM!");
    }
}
