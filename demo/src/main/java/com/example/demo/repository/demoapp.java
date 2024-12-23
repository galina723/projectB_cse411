package com.example.demo.repository;
import java.util.*;
import java.io.*;

public class demoapp {
    static Scanner keyboard = new Scanner(System.in);
    public static void a(String ab) {
        int n = keyboard.nextInt();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int a = keyboard.nextInt();
            int b = keyboard.nextInt();
            map.put(a, b);
        }
        int q = keyboard.nextInt();
        for (int i = 0; i < q; i++) {
            int c = keyboard.nextInt();
            if (map.containsKey(c)) {
                System.out.println(map.get(c));
            } else {
                System.out.println(-1);
            }
        }  
    }
}
