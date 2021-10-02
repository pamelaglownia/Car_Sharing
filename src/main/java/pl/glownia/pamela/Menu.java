package pl.glownia.pamela;

import java.util.ArrayList;
import java.util.List;

class Menu {

    private final Input input;
    private final Printer printer;
    private final CarSharingJDBC database;
    private final CompanyDao companyTable;
    private final CarTable carTable;
    private final List<Company> companies = new ArrayList<>();

    Menu() {
        input = new Input();
        printer = new Printer();
        String dataBaseFileName = input.getDataBaseFileName();
        database = new CarSharingJDBC(dataBaseFileName);
        companyTable = new CompanyDao(companies);
        carTable = new CarTable(new ArrayList<>());
    }

    void runMenu() {
        companyTable.createTable(database);
        carTable.createTable(database);
        logAsManager();
    }

    private void logAsManager() {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 1);
        switch (userDecision) {
            case 0:
                companyTable.closeConnection();
                System.exit(0);
            case 1:
                makeCompanyDecision();
                break;
        }
    }

    private void makeCompanyDecision() {
        int userDecision;
        do {
            printer.printCompanyMenu();
            userDecision = input.takeUserDecision(0, 2);
            switch (userDecision) {
                case 0:
                    logAsManager();
                    break;
                case 1:
                    companyTable.getAll();
                    if (!companies.isEmpty()) {
                        chooseTheCompany();
                    }
                    break;
                case 2:
                    addNewCompanyToList();
                    break;
            }
            System.out.println();
        } while (userDecision != 0);
    }

    private void makeCarDecision(int companyIndex) {
        int userDecision;
        do {
            printer.printCarMenu(companies, companyIndex);
            userDecision = input.takeUserDecision(0, 2);
            switch (userDecision) {
                case 0:
                    makeCompanyDecision();
                    break;
                case 1:
                    carTable.getAll(companyIndex);
                    break;
                case 2:
                    carTable.addNewCar(companyIndex);
                    makeCarDecision(companyIndex);
                    break;
            }
            System.out.println();
        } while (userDecision != 0);
    }

    private void addNewCompanyToList() {
        System.out.println("Enter the company name:");
        String companyName = input.getNewItem();
        companyTable.insertRecordToTable(companyName);
        System.out.println();
        makeCompanyDecision();
    }

    private void chooseTheCompany() {
        int companyIndex = input.takeUserDecision(1, companies.size()) - 1;
        makeCarDecision(companyIndex);
    }
}