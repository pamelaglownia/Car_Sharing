package pl.glownia.pamela;

import java.util.Scanner;

public class Input {
    private final Scanner scanner = new Scanner(System.in);

    private String[] takeDataBaseName() {
        System.out.println("Enter database file name (to choose default database enter: \"-databaseFileName carsharing\"):");
        String dataBaseFileName = scanner.nextLine().trim();
        if (dataBaseFileName.equals("0")) {
            System.exit(0);
        }
        while (dataBaseFileName.equals("")) {
            System.out.println("Name can not be empty. Enter database file name:");
            dataBaseFileName = scanner.nextLine();
        }
        return dataBaseFileName.split(" ");
    }

    String getDataBaseFileName() {
        String[] arrayWithDataBaseFileName = takeDataBaseName();
        if (arrayWithDataBaseFileName.length > 1 && arrayWithDataBaseFileName[0].equals("-databaseFileName")) {
            return arrayWithDataBaseFileName[1];
        } else {
            return arrayWithDataBaseFileName[0];
        }
    }

    public int takeUserDecision(int firstNumber, int lastNumber) {
        int userDecision;
        while (!scanner.hasNextInt()) {
            System.out.println("Incorrect value. Enter number again:");
            scanner.next();
        }
        userDecision = scanner.nextInt();
        scanner.nextLine();
        while (!(userDecision >= firstNumber && userDecision <= lastNumber)) {
            System.out.println("Choose one option from " + firstNumber + " to " + lastNumber + ":");
            userDecision = takeUserDecision(firstNumber, lastNumber);
        }
        return userDecision;
    }

    public String getNewItem() {
        String itemName = scanner.nextLine();
        while (itemName.equals("")) {
            System.out.println("Name can not be empty. Enter again:");
            itemName = scanner.nextLine();
        }
        return itemName.trim();
    }
}