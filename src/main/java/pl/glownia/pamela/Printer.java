package pl.glownia.pamela;

import java.util.List;

class Printer {

    void printMainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");
    }

    void printCompanyMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }

    void printCarMenu(List<Company> companies, int userDecision) {
        System.out.println("'" + companies.get(userDecision).getName() + "' company:");
        System.out.println("1. Car list\n" +
                "2. Create a car\n" +
                "0. Back");
    }
}