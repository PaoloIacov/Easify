package view;

import java.util.List;
import java.util.Scanner;

public class GeneralUtils {

    private GeneralUtils() {
    }

    public static void printList(List<String> items, String title) {
        if (items == null || items.isEmpty()) {
            System.out.println("La lista è vuota.");
            return;
        }

        if (title != null && !title.isEmpty()) {
            System.out.println(title);
        }

        for (int i = 0; i < items.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + items.get(i));
        }
    }


    public static String selectUsername(Scanner scanner, List<String> usernames, String errorMessage) {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= usernames.size()) {
                return usernames.get(choice - 1);
            }
        } catch (NumberFormatException e) {
            System.out.println(errorMessage);
        }
        return null;
    }
}
