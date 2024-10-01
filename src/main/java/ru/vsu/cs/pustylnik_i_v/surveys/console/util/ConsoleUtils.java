package ru.vsu.cs.pustylnik_i_v.surveys.console.util;

import java.util.Scanner;

public class ConsoleUtils {

    public static Boolean confirm(String subject) {
        System.out.printf("Are you sure you want to %s? (y/n)", subject);

        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
            return true;
        } else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
            return false;
        }
        return null;
    }

    public static String inputString(String subject) {
        System.out.printf("Enter %s: ", subject);

        Scanner scanner = new Scanner(System.in);

        return scanner.nextLine();
    }

    public static Integer inputInt(String subject) {
        System.out.printf("Enter %s: ", subject);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
