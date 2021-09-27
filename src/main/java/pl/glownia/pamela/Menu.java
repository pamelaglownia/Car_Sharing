package pl.glownia.pamela;

import java.sql.Connection;

public class Menu {

    private final Input input = new Input();
    private final Printer printer = new Printer();
    private final CarSharingJDBC database = new CarSharingJDBC();
    private Connection connection;
    private int id = 1;


    void runMenu() {
        String dataBaseFileName = input.getDataBaseFileName();
        connection = database.getConnection(dataBaseFileName);
        database.createTable(connection);
        logAsManager();
    }

    void logAsManager() {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 1);
        if (userDecision == 1) {
            makeManagerDecision();
        } else {
            System.exit(0);
        }
    }

    void makeManagerDecision() {
        printer.printManagerMenu();
        int userDecision = input.takeUserDecision(0, 2);
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
    }

    void manageCompanyList() {
        printer.printCompanyList();
        int userDecision = input.takeUserDecision(1, 3);
        switch (userDecision) {
            case 1:
                System.out.println("First...");
                break;
            case 2:
                System.out.println("Second...");
                break;
            case 3:
                System.out.println("Third...");
                break;
        }
    }

    void addNewCompanyToList() {
        String companyName = input.getNewCompanyName();
        database.insertRecordToTable(connection, id, companyName);
        id += 1;
        makeManagerDecision();
    }
}