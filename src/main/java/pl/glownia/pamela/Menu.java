package pl.glownia.pamela;

import pl.glownia.pamela.car.CarService;
import pl.glownia.pamela.company.CompanyService;
import pl.glownia.pamela.customer.CustomerService;


class Menu {
    private final DataBaseConnection dataBaseConnection;
    private CompanyService companyService;
    private CarService carService;
    private CustomerService customerService;
    private UserDecision userDecision;

    public Menu() {
        dataBaseConnection = new DataBaseConnection();
    }

    void run() {
        dataBaseConnection.createConnection();
        companyService = new CompanyService(dataBaseConnection);
        carService = new CarService(dataBaseConnection);
        customerService = new CustomerService(dataBaseConnection);
        userDecision = new UserDecision();
        System.out.println();
        runInitialMenu();
    }

    private void runInitialMenu() {
        int option = userDecision.chooseOptionFromInitialMenu();
        System.out.println();
        switch (option) {
            case 0:
                closeConnections();
                System.exit(0);
            case 1:
                makeManagerDecision();
                System.out.println();
                break;
            case 2:
                int customerId = customerService.chooseTheCustomer();
                System.out.println();
                if (customerId != 0) {
                    makeCustomerDecision(customerId);
                } else {
                    runInitialMenu();
                }
                break;
        }
    }

    private void makeManagerDecision() {
        int option;
        do {
            option = userDecision.chooseOptionFromManagerMenu();
            System.out.println();
            switch (option) {
                case 1:
                    int companyId = companyService.chooseTheCompany();
                    if (companyId != 0) {
                        System.out.println();
                        makeCarDecision(companyId);
                    }
                    break;
                case 2:
                    companyService.addNewCompany();
                    break;
                case 3:
                    int companyToDelete = companyService.chooseTheCompany();
                    if (carService.isEmptyList(companyToDelete)) {
                        companyService.deleteCompanyFromList(companyToDelete);
                    } else {
                        System.out.println("You can't delete company with cars.");
                    }
                    break;
                case 4:
                    customerService.addNewCustomer();
                    break;
                case 5:
                    customerService.deleteChosenCustomer();
                    break;
            }
            System.out.println();
        }
        while (option != 0);
        runInitialMenu();
    }

    private void makeCarDecision(int companyId) {
        int option;
        do {
            companyService.getCompanyName(companyId);
            option = userDecision.chooseOptionFromCarMenu();
            System.out.println();
            switch (option) {
                case 1:
                    carService.getAll(companyId);
                    break;
                case 2:
                    carService.addNewCar(companyId);
                    System.out.println();
                    break;
                case 3:
                    carService.deleteChosenCar(companyId);
            }
            System.out.println();
        } while (option != 0);
        makeManagerDecision();
    }

    private void makeCustomerDecision(int customerId) {
        int option;
        do {
            option = userDecision.chooseOptionFromCustomerMenu();
            System.out.println();
            switch (option) {
                case 1:
                    if (customerService.carIsAlreadyRented(customerId)) {
                        System.out.println("You've already rented a car. Return the car before you rent another one.\n");
                        break;
                    }
                    int chosenCompany = companyService.chooseTheCompany();
                    int chosenCar = carService.chooseTheCar(chosenCompany);
                    if (!carService.isEmptyList(chosenCompany) && !companyService.isEmptyList() && chosenCar != 0) {
                        customerService.rentChosenCar(customerId, chosenCar, chosenCompany);
                        carService.updateInformationAfterRentingCar(customerId, chosenCar, chosenCompany);
                        System.out.println();
                    } else {
                        System.out.println("Renting this car is not possible.");
                    }
                    break;
                case 2:
                    customerService.returnRentedCar(customerId);
                    System.out.println();
                    break;
                case 3:
                    carService.getInfoAboutRentedCar(customerId);
                    System.out.println();
                    break;
            }
        } while (option != 0);
        runInitialMenu();
    }

    private void closeConnections() {
        companyService.closeCompanyConnection();
        carService.closeCarConnection();
        customerService.closeCustomerConnection();
        System.out.println("Closing...");
    }
}