package pl.glownia.pamela;

public class Printer {

    void printMainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");
    }

    void printManagerMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

    void printCompanyList() {
        System.out.println("Company list:\n" +
                "1. First company name\n" +
                "2. Second company name\n" +
                "3. Third company name");
    }
}