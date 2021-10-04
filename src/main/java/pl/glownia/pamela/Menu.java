package pl.glownia.pamela;

import java.util.ArrayList;
import java.util.List;

class Menu {

    private final Input input;
    private final Printer printer;
    private final List<Company> companies = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final CompanyTable companyTable = new CompanyTable(companies);
    private final CarTable carTable = new CarTable(cars);
    private final CustomerTable customerTable = new CustomerTable(customers);

    Menu() {
        input = new Input();
        printer = new Printer();
    }

    void createConnection(String dataBaseFileName) {
        CarSharingJDBC database = new CarSharingJDBC(dataBaseFileName);
        companyTable.createTable(database);
        carTable.createTable(database);
        customerTable.createTable(database);
    }

    void run() {
        String dataBaseFileName = input.getDataBaseFileName();
        createConnection(dataBaseFileName);
        runInitialMenu();
    }

    private void runInitialMenu() {
        printer.printMainMenu();
        int userDecision = input.takeUserDecision(0, 3);
        switch (userDecision) {
            case 0:
                closeConnections();
                System.exit(0);
            case 1:
                makeCompanyDecision();
                break;
            case 2:
                customerTable.getAll();
                if (!customers.isEmpty()) {
                    int customerId = customerTable.chooseTheCustomer();
                    if (customerId != 0) {
                        makeCustomerDecision(customerId);
                    } else {
                        runInitialMenu();
                    }
                }
                break;
            case 3:
                customerTable.addNewCustomer();
                runInitialMenu();
                break;
        }
    }

    private void makeCompanyDecision() {
        int userDecision;
        do {
            printer.printCompanyMenu();
            userDecision = input.takeUserDecision(0, 2);
            switch (userDecision) {
                case 1:
                    makeCarDecision(companyTable.chooseTheCompany());
                    break;
                case 2:
                    companyTable.addNewCompany();
                    makeCompanyDecision();
                    break;
            }
            System.out.println();
        }
        while (userDecision != 0);
        runInitialMenu();

    }

    private void makeCarDecision(int companyIndex) {
        int userDecision;
        do {
            companyTable.getCompanyName(companyIndex);
            printer.printCarMenu();
            userDecision = input.takeUserDecision(0, 2);
            switch (userDecision) {
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
        makeCompanyDecision();
    }

    private void makeCustomerDecision(int customerId) {
        int userDecision;
        do {
            printer.printCustomerMenu();
            userDecision = input.takeUserDecision(0, 3);
            System.out.println(userDecision);
            switch (userDecision) {
                case 0:
                    break;
                case 1:
                    int chosenCompany = companyTable.chooseTheCompany();
                    int chosenCar = carTable.chooseTheCar(chosenCompany);
                    customerTable.rentACar(customerId, chosenCar);
                    carTable.getCarName(chosenCar);
                    break;
                case 2:
                    System.out.println("In progress....");
                    break;
                case 3:
                    System.out.println("In progress....");
                    break;
            }
            System.out.println();
        } while (userDecision != 0);
        runInitialMenu();
    }

    private void closeConnections() {
        companyTable.closeConnection();
        carTable.closeConnection();
        System.out.println("Closing...");
    }
}