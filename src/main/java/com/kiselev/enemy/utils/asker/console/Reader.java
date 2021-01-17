package com.kiselev.enemy.utils.asker.console;

import java.util.Scanner;

public class Reader {

    public static String ask(String question) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(question);
        return scanner.nextLine();
    }
}
