package pl.glownia.pamela;

import java.util.ArrayList;
import java.util.List;

class Menu {

    private final Input input;
    private final Printer printer;
    private final List<Company> companies = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final CompanyTable companyTable = new CompanyTable(companies);
    private final CarTable carTable = new CarTable(cars);

    Menu() {
        input = new Input();
        printer = new Printer();
    }

    void createConnection(String dataBaseFileName) {
        CarSharingJDBC database = new CarSharingJDBC(dataBaseFileName);
        companyTable.createTable(database);
        carTable.createTable(database);
    }

    void runMenu() {
        logAsManager(true);
    }

    private void logAsManager(boolean isInitialMenu) {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 1);
        switch (userDecision) {
            case 0:
                companyTable.closeConnection();
                carTable.closeConnection();
                System.out.println("Closing...");
                System.exit(0);
            case 1:
                if (isInitialMenu) {
                    String dataBaseFileName = input.getDataBaseFileName();
                    createConnection(dataBaseFileName);
                }
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
                    logAsManager(false);
                    break;
                case 1:
                    companyTable.getAll();
                    if (!companies.isEmpty()) {
                        chooseTheCompany();
                    }
                    break;
                case 2:
                    companyTable.addNewCompany();
                    makeCompanyDecision();
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

    private void chooseTheCompany() {
        int companyIndex = input.takeUserDecision(1, companies.size()) - 1;
        makeCarDecision(companyIndex);
    }
}