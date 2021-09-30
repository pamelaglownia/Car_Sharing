package pl.glownia.pamela;

import java.util.ArrayList;
import java.util.List;

class Menu {

    private final Input input;
    private final Printer printer;
    private final CarSharingJDBC database;
    private final CompanyDao companyTable;
    private final List<Company> companies = new ArrayList<>();

    Menu() {
        input = new Input();
        printer = new Printer();
        String dataBaseFileName = input.getDataBaseFileName();
        database = new CarSharingJDBC(dataBaseFileName);
        companyTable = new CompanyDao();
    }

    void runMenu() {
        companyTable.createTable(database);
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
                    companyTable.getAll(database, companies);
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
        companyTable.insertRecordToTable(database, companyName);
        System.out.println();
        makeManagerDecision();
    }
}