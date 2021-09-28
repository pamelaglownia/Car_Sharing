package pl.glownia.pamela;

import java.sql.Connection;

public class Menu {

    private final Input input = new Input();
    private final Printer printer = new Printer();
    private final CarSharingJDBC database = new CarSharingJDBC();
    private Connection connection;


    void runMenu() {
        String dataBaseFileName = input.getDataBaseFileName();
        connection = database.getConnection(dataBaseFileName);
        database.createTable(connection);
        logAsManager();
    }

    private void logAsManager() {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 1);
        if (userDecision == 1) {
            makeManagerDecision();
        } else {
            System.exit(0);
        }
    }

    private void makeManagerDecision() {
        int userDecision;
        do {
            printer.printManagerMenu();
            userDecision = input.takeUserDecision(0, 2);
            switch (userDecision) {
                case 1:
                    database.readRecords(connection);
                    break;
                case 2:
                    addNewCompanyToList();
                    break;
                default:
                    logAsManager();
            }
            System.out.println();
        } while (userDecision != 0);
    }

    private void addNewCompanyToList() {
        String companyName = input.getNewCompanyName();
        database.insertRecordToTable(connection, companyName);
        System.out.println();
        makeManagerDecision();
    }
}