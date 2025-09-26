// java
package ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class Console {
    private static final String LINE = "------------------------------------------------------------";
    private static final Scanner IN = new Scanner(System.in);

    private Console() {}

    public static void header(String title) {
        System.out.println();
        System.out.println(LINE);
        System.out.println("  " + title);
        System.out.println(LINE);
    }

    public static void line() {
        System.out.println(LINE);
    }

    public static String prompt(String label) {
        System.out.print(label);
        return IN.nextLine().trim();
    }

    public static BigDecimal promptBigDecimal(String label) {
        while (true) {
            try {
                String s = prompt(label);
                return new BigDecimal(s);
            } catch (Exception e) {
                System.out.println("Entrée invalide (nombre).");
            }
        }
    }

    public static LocalDate promptDate(String label, boolean optional) {
        while (true) {
            try {
                String s = prompt(label);
                if (optional && s.isEmpty()) return null;
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Format date invalide. Attendu: yyyy-MM-dd");
            }
        }
    }

    public static void pause() {
        System.out.println();
        System.out.print("Appuyez Entrée pour continuer...");
        IN.nextLine();
    }
}
