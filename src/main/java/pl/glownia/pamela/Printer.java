package pl.glownia.pamela;

class Printer {

    void printMainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");
    }

    void printManagerMenu() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
    }
}