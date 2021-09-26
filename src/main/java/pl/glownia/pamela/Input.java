package pl.glownia.pamela;

import java.util.Scanner;

class Input {
    private final Scanner scanner = new Scanner(System.in);

    String[] takeDataBaseName() {
        System.out.println("Enter database file name:");
        String dataBaseFileName = scanner.nextLine().trim();
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

    int takeUserDecision(int firstNumber, int lastNumber) {
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

    String getNewCompanyName() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        while (companyName.equals("")) {
            System.out.println("Name can not be empty. Enter database file name:");
            companyName = scanner.nextLine();
        }
        return companyName.trim();
    }
}