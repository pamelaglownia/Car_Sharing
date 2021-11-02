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
    private Input input;

    public Menu() {
        dataBaseConnection = new DataBaseConnection();
    }

    void run() {
        dataBaseConnection.createConnection();
        companyService = new CompanyService(dataBaseConnection);
        carService = new CarService(dataBaseConnection);
        customerService = new CustomerService(dataBaseConnection);
        userDecision = new UserDecision();
        input = new Input();
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
                customerService.getAll();
                int customerId = customerService.chooseTheCustomer(input.takeUserDecision(0, customerService.getCustomersListSize()));
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
                    companyService.getAll();
                    int companyId = companyService.chooseTheCompany(input.takeUserDecision(0, companyService.getCompaniesListSize()));
                    if (companyId != 0) {
                        System.out.println();
                        makeCarDecision(companyId);
                    }
                    break;
                case 2:
                    System.out.println("Enter the company name:");
                    companyService.addNewCompany(input.getNewItem());
                    break;
                case 3:
                    companyService.getAll();
                    int companyToDelete = companyService.chooseTheCompany(input.takeUserDecision(0, companyService.getCompaniesListSize()));
                    if (carService.isEmptyList(companyToDelete)) {
                        companyService.deleteCompanyFromList(companyToDelete);
                    }
                    break;
                case 4:
                    System.out.println("Enter the customer name:");
                    customerService.addNewCustomer(input.getNewItem());
                    break;
                case 5:
                    customerService.getAll();
                    int chosenCustomer = customerService.chooseTheCustomer(input.takeUserDecision(0, customerService.getCustomersListSize()));
                    customerService.deleteChosenCustomer(chosenCustomer);
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
                    System.out.println("Enter the car name:");
                    carService.addNewCar(input.getNewItem(), companyId);
                    System.out.println();
                    break;
                case 3:
                    carService.getAll(companyId);
                    System.out.println("0. Back");
                    int carId = carService.chooseTheCar(companyId, input.takeUserDecision(0, carService.getCarsListSize(companyId)));
                    if (carId == 0) {
                        break;
                    }
                    carService.deleteChosenCar(carId, companyId);
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
                    companyService.getAll();
                    int chosenCompany = companyService.chooseTheCompany(input.takeUserDecision(0, companyService.getCompaniesListSize()));
                    if (chosenCompany != 0 && !carService.isEmptyList(chosenCompany)) {
                        carService.getAll(chosenCompany);
                        System.out.println("0. Back");
                        int chosenCar = carService.chooseTheCar(chosenCompany, input.takeUserDecision(0, carService.getCarsListSize(chosenCompany)));
                        boolean isAvailable = carService.isAvailableForRent(chosenCar, chosenCompany);
                        while (!isAvailable) {
                            System.out.println("You can't choose this car. Choose other one or enter 0 to exit:");
                            chosenCar = carService.chooseTheCar(chosenCompany, input.takeUserDecision(0, carService.getCarsListSize(chosenCompany)));
                            if (chosenCar == 0) {
                                break;
                            }
                            isAvailable = carService.isAvailableForRent(chosenCar, chosenCompany);
                        }
                        if (chosenCar != 0) {
                            if (!carService.isEmptyList(chosenCompany) && !companyService.isEmptyList()) {
                                customerService.rentChosenCar(customerId, chosenCar, chosenCompany);
                                carService.updateInformationAfterRentingCar(customerId, chosenCar, chosenCompany);
                                System.out.println();
                            } else {
                                System.out.println("Renting this car is not possible.");
                            }
                        }
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