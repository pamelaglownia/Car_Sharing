package pl.glownia.pamela;

import java.util.ArrayList;
import java.util.List;

class Menu {

    private final Input input = new Input();
    private final List<Company> companies = new ArrayList<>();
    private final List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private final CompanyTable companyTable = new CompanyTable(companies);
    private final CarTable carTable = new CarTable(cars);
    private final CustomerTable customerTable = new CustomerTable(customers);

    private void createConnection(String dataBaseFileName) {
        CarSharingJDBC database = new CarSharingJDBC(dataBaseFileName);
        companyTable.createTable(database);
        carTable.createTable(database);
        customerTable.createTable(database);
    }

    void run() {
        String dataBaseFileName = input.getDataBaseFileName();
        createConnection(dataBaseFileName);
        System.out.println();
        runInitialMenu();
    }

    private void runInitialMenu() {
        MainMenuOption.printMainMenu();
        int userDecision = MainMenuOption.checkUserDecision(input.takeUserDecision(0, 2));
        System.out.println();
        switch (userDecision) {
            case 0:
                closeConnections();
                System.exit(0);
            case 1:
                makeManagerDecision();
                break;
            case 2:
                customerTable.getAll();
                if (!customers.isEmpty()) {
                    int customerId = customerTable.chooseTheCustomer();
                    System.out.println();
                    if (customerId != 0) {
                        makeCustomerDecision(customerId);
                    } else {
                        runInitialMenu();
                    }
                } else {
                    runInitialMenu();
                }
                break;
        }
    }

    private void makeManagerDecision() {
        int userDecision;
        do {
            ManagerMenuOption.printManagerMenu();
            userDecision = ManagerMenuOption.checkUserDecision(input.takeUserDecision(0, 5));
            System.out.println();
            switch (userDecision) {
                case 1:
                    int companyId = companyTable.chooseTheCompany();
                    if (companyId != 0) {
                        makeCarDecision(companyId);
                    }
                    break;
                case 2:
                    companyTable.addNewCompany();
                    System.out.println();
                    makeManagerDecision();
                    break;
                case 3:
                    customerTable.addNewCustomer();
                    System.out.println();
                    runInitialMenu();
                    break;
                case 4:
                    System.out.println("In progress...");
                    break;
                case 5:
                    System.out.println("In progress...");
                    break;
            }
        }
        while (userDecision != 0);
        runInitialMenu();

    }

    private void makeCarDecision(int companyIndex) {
        int userDecision;
        do {
            companyTable.getCompanyName(companyIndex);
            CarMenuOption.printCarMenu();
            userDecision = CarMenuOption.checkUserDecision(input.takeUserDecision(0, 3));
            System.out.println();
            switch (userDecision) {
                case 1:
                    carTable.getAll(companyIndex);
                    break;
                case 2:
                    carTable.addNewCar(companyIndex);
                    System.out.println();
                    makeCarDecision(companyIndex);
                    break;
                case 3:
                    carTable.deleteCar(companyIndex);
            }
            System.out.println();
        } while (userDecision != 0);
        makeManagerDecision();
    }

    private void makeCustomerDecision(int customerId) {
        int userDecision;
        do {
            CustomerMenuOption.printCustomerMenu();
            userDecision = CustomerMenuOption.checkUserDecision(input.takeUserDecision(0, 3));
            System.out.println();
            switch (userDecision) {
                case 1:
                    if (customerTable.customerRentedACar(customerId)) {
                        System.out.println("You've already rented a car. Return the car and then you will able to rent another one.\n");
                        break;
                    }
                    int chosenCompany = companyTable.chooseTheCompany();
                    int chosenCar = carTable.chooseTheCar(chosenCompany);
                    if (carTable.conditionsToRentAreMet(companyTable, carTable, chosenCompany, chosenCar)) {
                        customerTable.rentACar(customerId, chosenCar, chosenCompany);
                        carTable.updateInformationAboutCar(customerId, chosenCar, chosenCompany);
                        System.out.println();
                    }
                    break;
                case 2:
                    customerTable.returnRentedCar(customerId);
                    System.out.println();
                    break;
                case 3:
                    customers = customerTable.readRecords();
                    carTable.getRentedCarInfo(customerId);
                    System.out.println();
                    break;
            }
        } while (userDecision != 0);
        runInitialMenu();
    }

    private void closeConnections() {
        companyTable.closeConnection();
        carTable.closeConnection();
        customerTable.closeConnection();
        System.out.println("Closing...");
    }
}