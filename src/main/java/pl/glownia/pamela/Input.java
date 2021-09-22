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
}
