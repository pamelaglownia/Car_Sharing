package pl.glownia.pamela;

class Printer {

    void printMainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
    }

    void printCompanyMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

    void printCarMenu() {
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
    }

    void printCustomerMenu() {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
    }
}